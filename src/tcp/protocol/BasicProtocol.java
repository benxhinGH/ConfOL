package tcp.protocol;

import java.io.ByteArrayOutputStream;
import java.net.ProtocolException;

import tcp.Config;
import tcp.SocketUtil;

public abstract class BasicProtocol {
	// ���Ⱦ����ֽڣ�byte��Ϊ��λ
    public static final int LENGTH_LEN = 4;       //��¼�������ݳ�����ֵ�ĳ���
    protected static final int VER_LEN = 1;       //Э��İ汾���ȣ�����ǰ3λ��ΪԤ��λ����5λ��Ϊ�汾�ţ�
    protected static final int TYPE_LEN = 1;      //Э����������ͳ���

    private int reserved = 0;                     //Ԥ����Ϣ
    private int version = Config.VERSION;         //�汾��

    /**
     * ��ȡ�������ݳ���
     * ��λ���ֽڣ�byte��
     *
     * @return
     */
    protected int getLength() {
        return LENGTH_LEN + VER_LEN + TYPE_LEN;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * ��ȡЭ�����ͣ�������ʵ��
     *
     * @return
     */
    public abstract int getProtocolType();

    /**
     * ��Ԥ��ֵ�Ͱ汾�ż��������汾�ŵ�byte[]ֵ
     *
     * @return
     */
    private int getVer(byte r, byte v, int vLen) {
        int num = 0;
        int rLen = 8 - vLen;
        for (int i = 0; i < rLen; i++) {
            num += (((r >> (rLen - 1 - i)) & 0x1) << (7 - i));
        }
        return num + v;
    }

    /**
     * ƴ�ӷ������ݣ��˴�ƴ����Э��汾��Э�����ͺ����ݳ��ȣ�����������������ƴ��
     * ��˳��ƴ��
     *
     * @return
     */
    public byte[] genContentData() {
        byte[] length = SocketUtil.int2ByteArrays(getLength());
        byte reserved = (byte) getReserved();
        byte version = (byte) getVersion();
        byte[] ver = {(byte) getVer(reserved, version, 5)};
        byte[] type = {(byte) getProtocolType()};

        ByteArrayOutputStream baos = new ByteArrayOutputStream(LENGTH_LEN + VER_LEN + TYPE_LEN);
        baos.write(length, 0, LENGTH_LEN);
        baos.write(ver, 0, VER_LEN);
        baos.write(type, 0, TYPE_LEN);
        return baos.toByteArray();
    }

    /**
     * �������������ݳ���
     *
     * @param data
     * @return
     */
    protected int parseLength(byte[] data) {
        return SocketUtil.byteArrayToInt(data, 0, LENGTH_LEN);
    }

    /**
     * ������Ԥ��λ
     *
     * @param data
     * @return
     */
    protected int parseReserved(byte[] data) {
        byte r = data[LENGTH_LEN];//ǰ4���ֽڣ�0��1��2��3��Ϊ���ݳ��ȵ�intֵ����汾�����һ���ֽ�
        return (r >> 5) & 0xFF;
    }

    /**
     * �������汾��
     *
     * @param data
     * @return
     */
    protected int parseVersion(byte[] data) {
        byte v = data[LENGTH_LEN]; //��Ԥ��λ���һ���ֽ�
        return ((v << 3) & 0xFF) >> 3;
    }

    /**
     * ������Э������
     *
     * @param data
     * @return
     */
    public static int parseType(byte[] data) {
        byte t = data[LENGTH_LEN + VER_LEN];//ǰ4���ֽڣ�0��1��2��3��Ϊ���ݳ��ȵ�intֵ���Լ�verռһ���ֽ�
        return t & 0xFF;
    }

    /**
     * �����������ݣ��˴�������Э��汾��Э�����ͺ����ݳ��ȣ����������������ٽ���
     *
     * @param data
     * @return
     * @throws ProtocolException Э��汾��һ�£��׳��쳣
     */
    public int parseContentData(byte[] data) throws ProtocolException {
        int reserved = parseReserved(data);
        int version = parseVersion(data);
        int protocolType = parseType(data);
        if (version != getVersion()) {
            throw new ProtocolException("input version is error: " + version);
        }
        return LENGTH_LEN + VER_LEN + TYPE_LEN;
    }

    @Override
    public String toString() {
        return "Version: " + getVersion() + ", Type: " + getProtocolType();
    }

}
