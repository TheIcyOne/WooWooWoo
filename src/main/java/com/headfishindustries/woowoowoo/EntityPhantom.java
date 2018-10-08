package com.headfishindustries.woowoowoo;

import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPhantom extends EntityFlying{
	
	
	public EntityPhantom(World w) {
		super(w);
		this.setSize(0.7f, 1.1f);
		
		this.isImmuneToFire = false;
		this.experienceValue = 5;
		
		//this.moveHelper;
	}
	
	protected void initEntityAI() {
		
		//AIFlyCircle
		//AISwoop
		
		this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
	}
	
	public void tick() {
		super.tick();
		
		if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			this.remove();
		}
	}
	
	
	 public int getMaxSpawnedInChunk()
	 {
		 return 1;
	 }
	 
	
	public static class AIFlyCircle extends EntityAIBase{
		
		private final EntityPhantom par;

		public AIFlyCircle(EntityPhantom phantom) {
			this.par = phantom;
			this.setMutexBits(3);
		}
		
		@Override
		public boolean shouldExecute() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	public static class PhantomMoveHelper {
		private final EntityPhantom par;
		private int directionChangeCD;
		
		public boolean complete;
		
		private Vec3d startPos;
		private Action act;
		private Vec3d targetPos;
		
		private LinkedList<Vec3d> posLs;
		private Iterator<Vec3d> posIt;
		
		public PhantomMoveHelper(EntityPhantom phan, Vec3d target, Action a) {
			this.par = phan;
			this.startPos = phan.getPositionVector();
			this.act = a;
			this.targetPos = target;
			this.complete = false;
			
			
			switch(this.act) {
			case CIRCLE:
				this.posLs = genCircle(this.startPos, this.targetPos);
				break;
			case SWOOP:
				this.posLs = genCurve(this.startPos, this.targetPos);
				break;
				
			default:
				this.posLs = new LinkedList<Vec3d>();
				this.complete = true;
			}
			this.posIt = this.posLs.iterator();
		}
		
		
		//TODO: Swoop to curve. Probably simple parabola.
		LinkedList<Vec3d> genCurve(Vec3d start, Vec3d target){
			LinkedList<Vec3d> points = new LinkedList<Vec3d>();
			
			points.add(start);
			points.add(target);
			
			Vec3d end = new Vec3d(start.x + (target.x - start.x), start.y, start.z + (target.z - start.z));
			
			points.add(end);
			
			return points;
		}
		
		LinkedList<Vec3d> genCircle(Vec3d start, Vec3d centre){
			LinkedList<Vec3d> points = new LinkedList<Vec3d>();
			
			points.add(start);
			points.add(centre);
			points.add(start);
			
			return points;
		}
		
		static enum Action{
			SWOOP,
			CIRCLE
		}
		
		
	}

}
