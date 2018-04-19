package entity;

public class FileDescription {
	public static final int TYPE_CONF_FILE=0;
    public static final int TYPE_USER_HEAD_IMAGE=1;

    /**
     * �ļ����ͣ�0Ϊ�����ļ���1Ϊ�û�ͷ��ͼƬ
     */
    private int type;

    /**
     * ������Ϣ
     */
    private String additionInfo;

    public FileDescription(int type, String additionInfo) {
        this.type = type;
        this.additionInfo = additionInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAdditionInfo() {
        return additionInfo;
    }

    public void setAdditionInfo(String additionInfo) {
        this.additionInfo = additionInfo;
    }

    @Override
    public String toString() {
        return "FileDescription{" +
                "type=" + type +
                ", additionInfo='" + additionInfo + '\'' +
                '}';
    }

}
