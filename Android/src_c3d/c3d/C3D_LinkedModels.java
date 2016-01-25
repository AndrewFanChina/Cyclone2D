package c3d;
public class C3D_LinkedModels
{
  public C3D_ModelNode headModel;
  public static C3D_ModelNode modelStart,modelEnd;
  private int size=0;
  public C3D_LinkedModels()
  {
  }
  //���ո���ID��С�����˳����model
  public void addNode(C3D_Model model)
  {
    if(model.followRoadID==183)
    {
      System.out.println("");
    }
    if(headModel==null)
    {
      headModel=new C3D_ModelNode(model);
    }
    else
    {
      C3D_ModelNode newNode=new C3D_ModelNode(model);
      if(headModel.model.followRoadID>model.followRoadID)
      {
        setNodeAt(headModel,newNode);
      }
      else
      {
        C3D_ModelNode node=headModel;
        while(true)
        {
          if(node.model.followRoadID>model.followRoadID)
          {
            setNodeAt(node,newNode);
            break;
          }
          C3D_ModelNode nextNode=node.getNext();
          if(nextNode==null)
          {
            node.setNext(newNode);
            newNode.setPre(node);
            break;
          }
          else
          {
            node=nextNode;
          }
        }
      }
    }
    size++;
  }
  //��β����ͷ�������������γɻ�������
  public void linkStartAndEnd()
  {
    if(headModel==null)
    {
      return;
    }
    C3D_ModelNode node=headModel;
    while(node.getNext()!=null)
    {
      node=node.getNext();
    }
    node.setNext(headModel);
    headModel.setPre(node);
  }

  //���ݸ�����·��IDѰ����ʼģ�ͺͽ���ģ��
  public void searchNode(int followIDStart,int followIDEnd)
  {
    modelStart=null;
    modelEnd=null;
    if(headModel==null)
    {
      return;
    }
    C3D_ModelNode node=headModel;
    while(node.model.followRoadID<followIDStart)
    {
      node=node.getNext();
    }
    modelStart=node;
    if(followIDStart>followIDEnd)
    {
      int id=node.model.followRoadID;
      while(node.model.followRoadID>=id)
      {
        node=node.getNext();
      }
    }
    while(node.model.followRoadID<followIDEnd)
    {
      node=node.getNext();
    }
    modelEnd=node;
    //��¼��һ�ε�·����ʼ�ͽ���
    prefollowIDStart=followIDStart;
    prefollowIDEnd=followIDEnd;
  }
  //������һ��·����������ָ���������ʼ�ͽ���·��
  private int prefollowIDStart = -1;
  private int prefollowIDEnd = -1;
  public void searchNextNode(int followIDStart,int followIDEnd)
  {
    if(modelStart==null||modelEnd==null)
    {
      searchNode(followIDStart,followIDEnd);
      return;
    }
    //Ѱ����ʼ�ڵ�
    C3D_ModelNode node=modelStart;
    if(followIDStart<prefollowIDStart)//��������һȦ
    {
//      while(node.model.followRoadID>=modelStart.model.followRoadID)//��0
//      {
//        node=node.getNext();
//      }
      node=headModel;
    }
    while(node.model.followRoadID<followIDStart&&node.model.followRoadID<=node.getNext().model.followRoadID)
    {
      if(node.model instanceof C3D_ObjModel)
      {
        C3D_ObjModel obj=(C3D_ObjModel)node.model;
        if(obj.stagePropID==C3D_World.prop_others)
        {
          obj.lastY=-1;
        }
      }
      node=node.getNext();
    }
    modelStart=node;
    //Ѱ�ҽ����ڵ�
    node=modelEnd;
    if(followIDEnd<prefollowIDEnd)//��������һȦ
    {
//      while(node.model.followRoadID>=modelEnd.model.followRoadID)//��0
//      {
//        node=node.getNext();
//      }
      node=headModel;
    }
    while(node.model.followRoadID<followIDEnd&&node.model.followRoadID<=node.getNext().model.followRoadID)
    {
      node=node.getNext();
    }
    modelEnd=node;
    //��¼��һ�ε�·����ʼ�ͽ���
    prefollowIDStart=followIDStart;
    prefollowIDEnd=followIDEnd;
  }
  //���½ڵ��滻ԭ�ڵ�λ��
  public void setNodeAt(C3D_ModelNode oldNode,C3D_ModelNode newNode)
  {
    C3D_ModelNode pre=oldNode.getPre();
    if(pre!=null)
    {
      pre.setNext(newNode);
      newNode.setPre(pre);
    }
    oldNode.setPre(newNode);
    newNode.setNext(oldNode);
    if(oldNode.equals(headModel))
    {
      headModel=newNode;
    }
  }
  public int getSize()
  {
    return size;
  }
  public void releaseRes()
  {
    C3D_ModelNode node=headModel;
    while(node!=null)//��0
    {
      C3D_ModelNode oldNode=node;
      node=node.getNext();
      oldNode.releaseRes();
    }
    if(node!=null)
    {
      node.releaseRes();
    }
    headModel=null;
    size=0;
  }

}
