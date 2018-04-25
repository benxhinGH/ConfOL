package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import manager.SpeechSyncManager;
import tcp.callback.ResponseCallback;
import tcp.event.AuthEvent;
import tcp.protocol.BasicProtocol;
import tcp.protocol.DataAckProtocol;
import tcp.protocol.DataProtocol;
import tcp.protocol.PingAckProtocol;
import tcp.protocol.PingProtocol;
import util.GsonUtil;

public class ServerResponseTask implements Runnable {

    private ReciveTask reciveTask;
    private SendTask sendTask;
    private Socket socket;
    private ResponseCallback tBack;

    private volatile ConcurrentLinkedQueue<BasicProtocol> dataQueue = new ConcurrentLinkedQueue<>();
    private static ConcurrentHashMap<String, Socket> onLineClient = new ConcurrentHashMap<>();

    private String userIP;

    public String getUserIP() {
        return userIP;
    }

    public ServerResponseTask(Socket socket, ResponseCallback tBack) {
        this.socket = socket;
        this.tBack = tBack;
        this.userIP = socket.getInetAddress().getHostAddress();
        System.out.println("�û�IP��ַ��" + userIP);
    }

    @Override
    public void run() {
        try {
            //���������߳�
            reciveTask = new ReciveTask();
            reciveTask.inputStream = new DataInputStream(socket.getInputStream());
            reciveTask.start();

            //���������߳�
            sendTask = new SendTask();
            sendTask.outputStream = new DataOutputStream(socket.getOutputStream());
            sendTask.start();
            System.out.println("�����Ϊ��"+sendTask.outputStream.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (reciveTask != null) {
            reciveTask.isCancle = true;
            reciveTask.interrupt();
            if (reciveTask.inputStream != null) {
                SocketUtil.closeInputStream(reciveTask.inputStream);
                reciveTask.inputStream = null;
            }
            reciveTask = null;
        }

        if (sendTask != null) {
            sendTask.isCancle = true;
            sendTask.interrupt();
            if (sendTask.outputStream != null) {
                synchronized (sendTask.outputStream) {//��ֹд����ʱֹͣ��д����ͣ
                    sendTask.outputStream = null;
                }
            }
            sendTask = null;
        }
    }

    public void addMessage(BasicProtocol data) {
    	//System.out.println("�������������ݣ�"+data.toString());
        if (!isConnected()) {
            return;
        }

        dataQueue.offer(data);
        toNotifyAll(dataQueue);//���������������ݣ����ѷ����߳�
    }

    public Socket getConnectdClient(String clientID) {
        return onLineClient.get(clientID);
    }

    /**
     * ��ӡ�Ѿ����ӵĿͻ���
     */
    public static void printAllClient() {
        if (onLineClient == null) {
            return;
        }
        Iterator<String> inter = onLineClient.keySet().iterator();
        while (inter.hasNext()) {
            System.out.println("client:" + inter.next());
        }
    }

    public void toWaitAll(Object o) {
        synchronized (o) {
            try {
                o.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void toNotifyAll(Object obj) {
        synchronized (obj) {
            obj.notifyAll();
        }
    }

    public boolean isConnected() {
        if (socket.isClosed() || !socket.isConnected()) {
            onLineClient.remove(userIP);
            ServerResponseTask.this.stop();
            System.out.println("socket closed...");
            return false;
        }
        return true;
    }

    public class ReciveTask extends Thread {

        private DataInputStream inputStream;
        private boolean isCancle;

        @Override
        public void run() {
            while (!isCancle) {
                if (!isConnected()) {
                    isCancle = true;
                    break;
                }

                BasicProtocol clientData = SocketUtil.readFromStream(inputStream);

                if (clientData != null) {
                    if (clientData.getProtocolType() == 0) {
                        //System.out.println("dtype: " + ((DataProtocol) clientData).getDtype() + ", pattion: " + ((DataProtocol) clientData).getPattion() + ", msgId: " + ((DataProtocol) clientData).getMsgId() + ", data: " + ((DataProtocol) clientData).getData());
                        
                        DataAckProtocol dataAck = handleDataReceived((DataProtocol)clientData);
                        dataAck.setUnused("�յ���Ϣ��" + ((DataProtocol) clientData).getData());
                        dataQueue.offer(dataAck);
                        toNotifyAll(dataQueue); //���ѷ����߳�

                        tBack.targetIsOnline(userIP);
                    } else if (clientData.getProtocolType() == 2) {
                       // System.out.println("pingId: " + ((PingProtocol) clientData).getPingId());

                        PingAckProtocol pingAck = new PingAckProtocol();
                        pingAck.setUnused("�յ�����");
                        dataQueue.offer(pingAck);
                        toNotifyAll(dataQueue); //���ѷ����߳�

                        tBack.targetIsOnline(userIP);
                    }
                } else {
                    System.out.println("client is offline...");
                    break;
                }
            }

            SocketUtil.closeInputStream(inputStream);
        }
        
        private DataAckProtocol handleDataReceived(DataProtocol data){
        	DataAckProtocol ack=new DataAckProtocol();
        	int pattern=data.getPattion();
        	if(pattern==0){
        		AuthEvent authEvent=GsonUtil.getGson().fromJson(data.getData(),AuthEvent.class);
        		int roomId=authEvent.getRoomId();
        		int identity=authEvent.getIdentity();
        		if(identity==0){
        			SpeechSyncManager.getIntance().registerCreator(roomId, ServerResponseTask.this);
        		}else if(identity==1){
        			SpeechSyncManager.getIntance().registerParticipator(roomId, ServerResponseTask.this);
        		}
        		
        			
				ack.setAckMsgId(999);
				
        	}else{
        		SpeechSyncManager.getIntance().forwardEvent(ServerResponseTask.this, data);
        	}
        	
        	return ack;
        }
    }
    
    

    public class SendTask extends Thread {

        private DataOutputStream outputStream;
        private boolean isCancle;

        @Override
        public void run() {
            while (!isCancle) {
                if (!isConnected()) {
                    isCancle = true;
                    break;
                }

                BasicProtocol procotol = dataQueue.poll();
                if (procotol == null) {
                    toWaitAll(dataQueue);
                } else if (outputStream != null) {
                    synchronized (outputStream) {
                    	
                        SocketUtil.write2Stream(procotol, outputStream);
                    }
                }
            }

            SocketUtil.closeOutputStream(outputStream);
        }
    }
}
