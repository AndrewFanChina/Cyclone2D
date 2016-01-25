package c2d.mod.map.scroll;

import c2d.mod.C2D_FrameManager;
import c2d.mod.sprite.C2D_Sprite;

public interface C2D_SpriteLoader
{
	public C2D_Sprite loadSprite(C2D_FrameManager frame,short[] anteTypeID,int actionID,int frameID,int atX,int atY,int atZ);
	public void afterLoad(C2D_Sprite sprite);
}
