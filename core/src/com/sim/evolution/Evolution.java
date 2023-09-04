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
	OrthographicCamera camera;
	Rectangle bounds = new Rectangle(), safe = new Rectangle();
	SpriteBatch batch;
	ShapeRenderer sr;
	int amt = 100;
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
		System.out.println("aaaaaaaaaa");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 800);
		bounds.set(0, 0, 790, 790);
		safe.set(200, 200, 400, 400);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        
        for(int i = 0; i<amt; i++) 
        	cells.add(new Cell());
        

        
	}

	@Override
	public void render () {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			for(Cell cell : cells) {
				if(safe.contains(cell.pos)) 
					survivors.add(cell);
			}
			if(survivors.isEmpty()) {
				System.out.println("0% survival");
		        for(int i = 0; i<amt; i++) 
		        	cells.add(new Cell());
			}
			else {
				for(int i = 0; i<amt; i++) 
					children.add(new Cell(survivors.get(i%survivors.size)));
			}
			
			cells.clear();
			cells.addAll(children);
			children.clear();
			survivors.clear();
		}
		ScreenUtils.clear(58f/255f,58f/255f,58f/255f,1);
		sr.begin(ShapeRenderer.ShapeType.Line);
		sr.setColor(88f/255f,88f/255f,88f/255f,1);
		sr.rect(200, 200, 400, 400);
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
	Random random = new Random();
	public Cell() {
		rad=Math.toRadians(random.nextInt(0, 361));
		dir=new Vector2((float)Math.cos(rad), (float)Math.sin(rad)).nor();
		spe=random.nextFloat(0, 7);
		color=new Color(rInt(0,255)/255f,rInt(0,255)/255f,rInt(0,255)/255f,1);
	}
	public Cell(Cell parent) {
		rad=parent.rad+random.nextFloat(-20, 20);
		dir=new Vector2((float)Math.cos(rad), (float)Math.sin(rad)).nor();
		spe=parent.spe+random.nextFloat(-0.6f, 0.6f);
		color=parent.color.add(random.nextFloat(-10, 10)/255f, random.nextFloat(-10f, 10f)/255f, random.nextFloat(-10f, 10f)/255f, 1);
	}
	private float rInt(int org, int bound) {
		return random.nextInt(org, bound+1);
	}
	
}