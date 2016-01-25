package c2d.lang.io;


public interface C2D_SerializeObject
{
	void readObject(C2D_Serializable record);
	void writeObject(C2D_Serializable record);
}
