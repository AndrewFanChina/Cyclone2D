package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.mod.sprite.tween.C2D_SpriteManager;
import c2d.plat.gfx.C2D_ImageClip;

public class C2D_SpriteClip extends C2D_ImageClip
{
	public C2D_SpriteClip(C2D_SpriteManager spriteManager,DataInputStream dis) throws Exception
	{
		super();
		short index=0,_cx=0,_cy=0,_cw=0,_cH=0;
		index = (short) (C2D_IOUtil.readShort(index, dis) & 0xFFFF); //Í¼Æ¬Ë÷Òý
		_cx = (short) (C2D_IOUtil.readShort(_cx, dis) & 0xFFFF); // X
		_cy = (short) (C2D_IOUtil.readShort(_cy, dis) & 0xFFFF); // Y
		_cw = (short) (C2D_IOUtil.readShort(_cw, dis) & 0xFFFF); // W
		_cH = (short) (C2D_IOUtil.readShort(_cH, dis) & 0xFFFF); // H
		setImage(spriteManager.spriteImgs[index]);
		setContentRect(_cx,_cy, _cw, _cH);
		setShowSize(_cw, _cH);
	}

}
