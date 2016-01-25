package c3d;

import java.util.Vector;

import c3d.util.math.C3D_Vertex3;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class C3D_RoadModel extends C3D_Model
{
  private Vector path=new Vector();
  private static C3D_Vertex3 vertexTemp0=new C3D_Vertex3();
  private static C3D_Vertex3 vertexTemp1=new C3D_Vertex3();
  public C3D_RoadModel(byte modelTypeT, C3D_ModelData modelDataT)
  {
    super(modelTypeT, modelDataT);
  }
  public void createRoad()
  {
    if(this.modelData!=null)
    {
      path.removeAllElements();
      int faceLen=0;
      int faceID=0;
      short vID0,vID1,vID2,vID3;
      for(int i=0;i<modelData.faceDataList.length;i++)
      {
        faceLen=0;
        faceID=0;
        for(int j=0;j<modelData.faceDataList[i].length;j+=faceLen)
        {
          vID0=modelData.faceDataList[i][j];
          vID1=modelData.faceDataList[i][j+1];
          vID2=modelData.faceDataList[i][j+2];
          vID3=modelData.faceDataList[i][j+3];
          path.addElement(new short[]{vID1,vID2});
          faceLen=modelData.faceDataLens[i][faceID];
          faceID++;
        }
      }
    }
  }
  //根据路面ID获取路面中心(将路面中心返回值放在vertex3)
  public boolean getRoadCenter(int id,C3D_Vertex3 vertex3)
  {
    int count=path.size();
    if(count<0||vertex3==null)
    {
      return false;
    }
    if(id<0)
    {
      id+=((-id)/count+1)*count;
    }
    if(id>=count)
    {
      id%=count;
    }
    short roadPint[]=(short[])path.elementAt(id);
    int idL=roadPint[0];
    int idR=roadPint[1];
    getVertex(idL-1,vertexTemp0);
    getVertex(idR-1,vertexTemp1);
    vertex3.setValue(
        (vertexTemp0.x+vertexTemp1.x)/2,
        (vertexTemp0.y+vertexTemp1.y)/2,
        (vertexTemp0.z+vertexTemp1.z)/2);
      return true;
  }
  //根据ID获取路面左右顶点
  public boolean getRoadPoint(int id,C3D_Vertex3 vertex3R,C3D_Vertex3 vertex3L)
  {
    int count=path.size();
    if(count<0||vertex3L==null||vertex3R==null)
    {
      return false;
    }
    if(id<0)
    {
      id+=((-id)/count+1)*count;
    }
    if(id>=count)
    {
      id%=count;
    }
    short roadPoint[]=(short[])path.elementAt(id);
    int idR=roadPoint[0];
    int idL=roadPoint[1];
    getVertex(idR-1,vertex3R);
    getVertex(idL-1,vertex3L);
    return true;
  }
  //根据faceID获取顶点信息
  public void getVertex(int id,C3D_Vertex3 vertex3)
  {
    if(modelData!=null&&id>=0&&id<modelData.vertexList.length&&vertex3!=null)
    {
      id=id*C3D_ModelData.VERTEX_LEN;
      vertex3.setValue(modelData.vertexList[id],
                       modelData.vertexList[id+1],
                       modelData.vertexList[id+2]);
    }
  }
  //获取路面节点数目
  public int gerNodeCount()
  {
    return this.path.size();
  }
  //释放资源
  public void releaseRes()
  {
    super.releaseRes();
    if(path!=null)
    {
      path.removeAllElements();
      path=null;
    }
  }
}
