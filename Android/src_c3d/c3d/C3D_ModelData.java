package c3d;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c3d.util.math.C3D_Vertex3;
public class C3D_ModelData
{
    public String dataName;//ģ�����ƣ�ֻ�е�ģ����ͨ��ģ��ʱ�Ŵ���
    public long[] vertexList = null;
    public int faceColorList[];
    public short[][] faceDataList;
    public byte[][] faceDataLens;
    public static final int VERTEX_LEN=3;
    public boolean needLoad=false;//�Ƿ���Ҫ���أ��ǵ�����ģ�ͼ���Ҫ����
    public C3D_ModelData()
    {
    }
    public void readObject(DataInputStream s)
    {
      try {
        short len = 0;
        //���㼯��
        len = C2D_IOUtil.readShort(len, s);
        if (len < 0)
        {
          return;
        }
        vertexList = new long[len * VERTEX_LEN];
        int data = 0;
        short shortData = 0;
        byte byteData = 0;
        for (int i = 0; i < len; i++)
        {
          vertexList[data++] = C2D_IOUtil.readInt(data, s);
          vertexList[data++] = C2D_IOUtil.readInt(data, s);
          vertexList[data++] = C2D_IOUtil.readInt(data, s);
        }
        //����������
        len = C2D_IOUtil.readShort(len, s);
        faceDataList = new short[len][];
        faceDataLens = new byte[len][];
        faceColorList = new int[len];
        short lenFace = 0;
        for (int i = 0; i < len; i++) {
          faceColorList[i] = C2D_IOUtil.readInt(data, s); //��ȡ��i����������ɫ
          lenFace = C2D_IOUtil.readShort(lenFace, s); //��ȡ��i���������������Ķ��������Ŀ
          faceDataLens[i] = new byte[lenFace];
          int totalCount = 0;
          for (int j = 0; j < lenFace; j++) {
            faceDataLens[i][j] = C2D_IOUtil.readByte(byteData, s);
            totalCount += faceDataLens[i][j];
          }
          faceDataList[i] = new short[totalCount];
          for (int j = 0; j < totalCount; j++) {
            faceDataList[i][j] = C2D_IOUtil.readShort(shortData, s);
          }
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
    //�ҳ�����ĵ�������
    private static C3D_Vertex3 vTemp = new C3D_Vertex3(0, 0, 0);
    public C3D_Vertex3 getModelBottomCenter()
    {
        long xMin = Long.MAX_VALUE;
        long yMin = Long.MAX_VALUE;
        long zMin = Long.MAX_VALUE;
        long xMax = Long.MIN_VALUE;
        long yMax = Long.MIN_VALUE;
        long zMax = Long.MIN_VALUE;
        for (int i = 0; i < vertexList.length;)
        {
          if(vertexList[i]<xMin)
          {
            xMin = vertexList[i];
          }
          if(vertexList[i]>xMax)
          {
            xMax = vertexList[i];
          }
          i++;
          if(vertexList[i]<yMin)
          {
            yMin = vertexList[i];
          }
          if(vertexList[i]>yMax)
          {
            yMax = vertexList[i];
          }
          i++;
          if(vertexList[i]<zMin)
          {
            zMin = vertexList[i];
          }
          if(vertexList[i]>zMax)
          {
            zMax = vertexList[i];
          }
          i++;
        }
        vTemp.setValue((xMax+xMin)/2,yMin,(zMax+zMin)/2);
        return vTemp;
    }
    //�ҳ����������
    public C3D_Vertex3 getModelCenter()
    {
        long xMin = Long.MAX_VALUE;
        long yMin = Long.MAX_VALUE;
        long zMin = Long.MAX_VALUE;
        long xMax = Long.MIN_VALUE;
        long yMax = Long.MIN_VALUE;
        long zMax = Long.MIN_VALUE;
        for (int i = 0; i < vertexList.length;)
        {
          if(vertexList[i]<xMin)
          {
            xMin = vertexList[i];
          }
          if(vertexList[i]>xMax)
          {
            xMax = vertexList[i];
          }
          i++;
          if(vertexList[i]<yMin)
          {
            yMin = vertexList[i];
          }
          if(vertexList[i]>yMax)
          {
            yMax = vertexList[i];
          }
          i++;
          if(vertexList[i]<zMin)
          {
            zMin = vertexList[i];
          }
          if(vertexList[i]>zMax)
          {
            zMax = vertexList[i];
          }
          i++;
        }
        vTemp.setValue((xMax+xMin)/2,(yMax+yMin)/2,(zMax+zMin)/2);
        return vTemp;
    }
    //�ҳ�����󶨰뾶
    public long getModelBoudingR()
    {
        long xMin = Long.MAX_VALUE;
        long yMin = Long.MAX_VALUE;
        long zMin = Long.MAX_VALUE;
        long xMax = Long.MIN_VALUE;
        long yMax = Long.MIN_VALUE;
        long zMax = Long.MIN_VALUE;
        for (int i = 0; i < vertexList.length;)
        {
          if(vertexList[i]<xMin)
          {
            xMin = vertexList[i];
          }
          if(vertexList[i]>xMax)
          {
            xMax = vertexList[i];
          }
          i++;
          if(vertexList[i]<yMin)
          {
            yMin = vertexList[i];
          }
          if(vertexList[i]>yMax)
          {
            yMax = vertexList[i];
          }
          i++;
          if(vertexList[i]<zMin)
          {
            zMin = vertexList[i];
          }
          if(vertexList[i]>zMax)
          {
            zMax = vertexList[i];
          }
          i++;
        }
        long r0=(xMax-xMin)/2;
        long r1=(yMax-yMin)/2;
        long r2=(zMax-zMin)/2;
        return Math.max(r2,Math.max(r0,r1));
    }
    //�ͷ���Դ
    public void releaseRes()
    {
      dataName=null;
      vertexList=null;
      faceColorList=null;
      faceDataList=null;
      faceDataLens=null;
    }
}
