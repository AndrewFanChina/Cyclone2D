package c3d;
public class C3D_ModelNode
{
  public C3D_Model model;
  private C3D_ModelNode preMode,nextNode;
  public C3D_ModelNode(C3D_Model modelT)
  {
    model=modelT;
  }
  public void setPre(C3D_ModelNode preModeT)
  {
    preMode=preModeT;
  }
  public C3D_ModelNode getPre()
  {
    return preMode;
  }
  public void setNext(C3D_ModelNode nextNodeT)
  {
    nextNode=nextNodeT;
  }
  public C3D_ModelNode getNext()
  {
    return nextNode;
  }
  public void releaseRes()
  {
    if(model!=null)
    {
      model.releaseRes();
      model = null;
    }
    preMode=null;
    nextNode=null;
  }
}
