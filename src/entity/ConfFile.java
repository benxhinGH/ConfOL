package entity;

public class ConfFile {
    int id;
    String confChannelId;
    String name;
    String path;

    public ConfFile(){}

    public ConfFile(int id, String confChannelId, String name, String path) {
        this.id = id;
        this.confChannelId = confChannelId;
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfChannelId() {
        return confChannelId;
    }

    public void setConfChannelId(String confChannelId) {
        this.confChannelId = confChannelId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

	@Override
	public String toString() {
		return "ConfFile [id=" + id + ", confChannelId=" + confChannelId
				+ ", name=" + name + ", path=" + path + "]";
	}
    
    
}

