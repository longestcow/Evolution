package com.sim.evolution;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class Evolution extends ApplicationAdapter {
	public static Random random = new Random();

	OrthographicCamera camera;
	Rectangle bounds = new Rectangle(), safe = new Rectangle();
	SpriteBatch batch;
	ShapeRenderer sr;
	int amt = 100;
	static int mutationChance = 500;
	int genCount = 0;
	
	//safe box
	int[][] sc = new int[][] {	{0,450,350,350},{225,450,350,350},{450,450,350,350},
								{0,225,350,350},{225,225,350,350},{450,225,350,350},
								{0,0,350,350},{225,0,350,350},{450,0,350,350},      
								{0,0,400,800},{400,0,400,800},{0,400,800,400},{0,0,800,400}};
	int curr = 3, cur;
	Array<Cell> cells = new Array<>(), survivors = new Array<>(), children = new Array<>();
//	Timer timer = new Timer("Timer");
//	TimerTask task = new TimerTask() {
//		@Override
//		public void run() {
//
//		}
//	};
//	
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 800);
		bounds.set(0, 0, 790, 790);
		safe.set(sc[curr][0], sc[curr][1], sc[curr][2], sc[curr][3]);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        
	}

	@Override
	public void render () {
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			for(Cell cell : cells) {
				if(safe.contains(cell.pos)) 
					survivors.add(cell);
			}
			if(survivors.isEmpty()) {
		        for(Cell cell : cells) {
		        	cell.pos=new Vector2(400,400);
		        	if(random.nextInt(1,mutationChance)==1) {
		    			cell.rad=Math.toRadians(Evolution.random.nextInt(0, 361));
		    			cell.dir=new Vector2((float)Math.cos(cell.rad), (float)Math.sin(cell.rad)).nor();
		    			cell.spe=Evolution.random.nextFloat(0, 7);
		        	}
		        	children.add(cell);
		        }
		        	
			}
			else {
				for(int i = 0; i<amt; i++) 
					children.add(new Cell(survivors.get(i%survivors.size)));
				System.out.println("gen "+ ++genCount);
			}
			
			
			cells.clear();
			cells.addAll(children);
			children.clear();
			survivors.clear();
		}
		
		checkInput();
		
		ScreenUtils.clear(58f/255f,58f/255f,58f/255f,1);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(88f/255f,88f/255f,88f/255f,1);
		sr.rect(sc[curr][0], sc[curr][1], sc[curr][2], sc[curr][3]);
		sr.end();
		camera.update();

		Vector2 tempPos;
		for(Cell cell : cells) {
			tempPos=new Vector2(cell.pos);
			tempPos.add(new Vector2(cell.dir).scl(cell.spe));
			if(bounds.contains(tempPos)) {
				cell.pos=tempPos;
			}
			drawCell(cell);
		}

	} 
	
	private void checkInput() {
		cur = curr;
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) 
			curr=0;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) 
			curr=1;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) 
			curr=2;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) 
			curr=3;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) 
			curr=4;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) 
			curr=5;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) 
			curr=6;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) 
			curr=7;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) 
			curr=8;
		
		else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) 
			curr=9;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) 
			curr=10;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) 
			curr=11;
		else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) 
			curr=12;
		
		else if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			cells.clear();
			for(int i = 0; i<amt; i++) {
				cells.add(new Cell());
			}
		}
		if(cur!=curr) 
			safe.set(sc[curr][0], sc[curr][1], sc[curr][2], sc[curr][3]);
		
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
	
	public void drawCell(Cell cell) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(cell.color);
        sr.rect(cell.pos.x, cell.pos.y, 10, 10);
        sr.end();
	}
}




class Cell {
	public Vector2 dir, pos=new Vector2(400,400);
	public float spe;
	public Color color;
	double rad;
	public Cell() {
		rad=Math.toRadians(rInt(0,360));
		dir=new Vector2((float)Math.cos(rad), (float)Math.sin(rad)).nor();
		spe=rFloat(0,7);
		color=new Color(rInt(0,255)/255f,rInt(0,255)/255f,rInt(0,255)/255f,1);
	}
	public Cell(Cell parent) {
		if(rInt(1,Evolution.mutationChance)==1) {
			rad=Math.toRadians(rInt(0,360));
			dir=new Vector2((float)Math.cos(rad), (float)Math.sin(rad)).nor();
			spe=rFloat(0, 7);
		}
		else {
			rad=parent.rad+rFloat(-0.2f, 0.2f);
			dir=new Vector2((float)Math.cos(rad), (float)Math.sin(rad)).nor();
			spe=parent.spe+rFloat(-0.6f, 0.6f);
		}
		color=new Color(parent.color);
		color.add(rFloat(-4,4)/255f, rFloat(-4,4)/255f, rFloat(-4,4)/255f, 1);
	}
	private int rInt(int org, int bound) {
		return Evolution.random.nextInt(org, bound+1);
	}
	private float rFloat(float org, float bound) {
		return Evolution.random.nextFloat(org, bound);
	}

	
}