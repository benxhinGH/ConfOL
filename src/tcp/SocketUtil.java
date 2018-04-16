package tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import tcp.protocol.BasicProtocol;
import tcp.protocol.DataAckProtocol;
import tcp.protocol.DataProtocol;
import tcp.protocol.PingAckProtocol;
import tcp.protocol.PingProtocol;

public class SocketUtil {
	
	private static Map<Integer, String> msgImp = new HashMap<>();

    static {
        msgImp.put(DataProtocol.PROTOCOL_TYPE, "tcp.protocol.DataProtocol");       //0
        msgImp.put(DataAckProtocol.PROTOCOL_TYPE, "tcp.protocol.DataAckProtocol"); //1
        msgImp.put(PingProtocol.PROTOCOL_TYPE, "tcp.protocol.PingProtocol");       //2
        msgImp.put(PingAckProtocol.PROTOCOL_TYPE, "tcp.protocol.PingAckProtocol"); //3
    }

    /**
     * ������������
     *
     * @param data
     * @return
     */
    public static BasicProtocol parseContentMsg(byte[] data) {
        int protocolType = BasicProtocol.parseType(data);
        String className = msgImp.get(protocolType);
        BasicProtocol basicProtocol;
        try {
            basicProtocol = (BasicProtocol) Class.forName(className).newInstance();
            basicProtocol.parseContentData(data);
        } catch (Exception e) {
            basicProtocol = null;
            e.printStackTrace();
        }
        return basicProtocol;
    }

    /**
     * ������
     *
     * @param inputStream
     * @return
     * @throws SocketExceptions
     */
    public static BasicProtocol readFromStream(InputStream inputStream) {
        BasicProtocol protocol;
        BufferedInputStream bis;

        //header�б�������������ݵĳ���ֵ��4���ֽڱ�ʾ��������write2Stream�����У�����д��header
        byte[] header = new byte[BasicProtocol.LENGTH_LEN];

        try {
            bis = new BufferedInputStream(inputStream);

            int temp;
            int len = 0;
            while (len < header.length) {
                temp = bis.read(header, len, header.length - len);
                if (temp > 0) {
                    len += temp;
                } else if (temp == -1) {
                    bis.close();
                    return null;
                }
            }

            len = 0;
            int length = byteArrayToInt(header);//���ݵĳ���ֵ
            byte[] content = new byte[length];
            while (len < length) {
                temp = bis.read(content, len, length - len);

                if (temp > 0) {
                    len += temp;
                }
            }

            protocol = parseContentMsg(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return protocol;
    }

    /**
     * д����
     *
     * @param protocol
     * @param outputStream
     */
    public static void write2Stream(BasicProtocol protocol, OutputStream outputStream) {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        byte[] buffData = protocol.genContentData();
        byte[] header = int2ByteArrays(buffData.length);
        try {
            bufferedOutputStream.write(header);
            bufferedOutputStream.write(buffData);
            bufferedOutputStream.flush();
            System.out.println("�����ݣ�"+protocol.toString()+"д�������:"+outputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �ر�������
     *
     * @param is
     */
    public static void closeInputStream(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �ر������
     *
     * @param os
     */
    public static void closeOutputStream(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] int2ByteArrays(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    public static int byteArrayToInt(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i)); //intռ4���ֽڣ�0��1��2��3��
        }
        return intValue;
    }

    public static int byteArrayToInt(byte[] b, int byteOffset, int byteCount) {
        int intValue = 0;
        for (int i = byteOffset; i < (byteOffset + byteCount); i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - (i - byteOffset)));
        }
        return intValue;
    }

    public static int bytes2Int(byte[] b, int byteOffset) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        byteBuffer.put(b, byteOffset, 4); //ռ4���ֽ�
        byteBuffer.flip();
        return byteBuffer.getInt();
    }
}


