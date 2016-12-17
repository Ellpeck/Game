package de.ellpeck.game.world;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.ellpeck.game.TheGame;
import de.ellpeck.game.tile.Tile;
import de.ellpeck.game.util.Direction;
import de.ellpeck.game.world.chunk.Chunk;

import java.util.Arrays;

public class ChunkMesh implements Disposable{

    private final Vector3 tempVec00 = new Vector3();
    private final Vector3 tempVec10 = new Vector3();
    private final Vector3 tempVec01 = new Vector3();
    private final Vector3 tempVec11 = new Vector3();
    private final Vector3 tempNormal = new Vector3();
    private final Vector2 tempUv00 = new Vector2();
    private final Vector2 tempUv10 = new Vector2();
    private final Vector2 tempUv01 = new Vector2();
    private final Vector2 tempUv11 = new Vector2();

    private static final float TEXTURE_SHEET_SIZE = 256F;
    private static final float TEXTURE_TILE_SIZE = 16F;
    private static final float TEX_TILE_SIZE = 1F/TEXTURE_SHEET_SIZE*TEXTURE_TILE_SIZE;

    private static final float CUBE_SIZE = 1F;

    private static final int POSITION_COMPONENTS = 3;
    private static final int TEXTURE_COORDS = 2;
    private static final int NORMAL_COMPONENTS = 3;
    private static final int COMPONENTS_TOTAL = POSITION_COMPONENTS+TEXTURE_COORDS+NORMAL_COMPONENTS;
    private static final int VERTEX_PER_TRIANGLE = 3;
    private static final int TRIANGLES_PER_FACE = 2;
    private static final int FACES_PER_CUBE = 6;
    private static final int MAX_VERTEX_DATA = VERTEX_PER_TRIANGLE*TRIANGLES_PER_FACE*FACES_PER_CUBE*Chunk.SIZE*Chunk.HEIGHT*Chunk.SIZE*COMPONENTS_TOTAL;

    private final float[] vertices = new float[MAX_VERTEX_DATA];

    private Mesh mesh;
    private final Chunk chunk;

    private int floatIndex;
    private int vertexCount;

    public ChunkMesh(Chunk chunk){
        this.chunk = chunk;
    }

    public void makeNewMesh(){
        if(this.mesh != null){
            this.mesh.dispose();

            this.vertexCount = 0;
            this.floatIndex = 0;
            Arrays.fill(this.vertices, 0);
        }

        for(int x = 0; x < Chunk.SIZE; x++){
            for(int z = 0; z < Chunk.SIZE; z++){
                for(int y = 0; y < Chunk.HEIGHT; y++){
                    Tile tile = this.chunk.getTile(x, y, z);
                    if(tile != null && tile.doesRender()){
                        this.addTile(tile, this.chunk.x*Chunk.SIZE+x, y, this.chunk.z*Chunk.SIZE+z, CUBE_SIZE);
                    }
                }
            }
        }

        this.mesh = new Mesh(true, this.vertexCount, 0,
                new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, TEXTURE_COORDS, "a_textureCoords"),
                new VertexAttribute(Usage.Normal, NORMAL_COMPONENTS, "a_normal"));
        this.mesh.setVertices(this.vertices, 0, this.floatIndex-1);

        TheGame.LOGGER.info("Made mesh for chunk at "+this.chunk.x+", "+this.chunk.z+". Totals: "+this.vertexCount+" vertices, "+(this.floatIndex-1)+" floats of max "+this.vertices.length+".");
    }

    private void addTile(Tile tile, int x, int y, int z, float size){
        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.NORTH)){
            this.tempVec00.set(0, 0, size).add(x, y, z);
            this.tempVec10.set(size, 0, size).add(x, y, z);
            this.tempVec11.set(size, size, size).add(x, y, z);
            this.tempVec01.set(0, size, size).add(x, y, z);
            this.tempNormal.set(0, 0, 1);
            this.addRect(tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.NORTH));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.SOUTH)){
            this.tempVec00.set(size, 0, 0).add(x, y, z);
            this.tempVec10.set(0, 0, 0).add(x, y, z);
            this.tempVec11.set(0, size, 0).add(x, y, z);
            this.tempVec01.set(size, size, 0).add(x, y, z);
            this.tempNormal.set(0, 0, -1);
            this.addRect(tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.SOUTH));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.DOWN)){
            this.tempVec00.set(0, 0, 0).add(x, y, z);
            this.tempVec10.set(size, 0, 0).add(x, y, z);
            this.tempVec11.set(size, 0, size).add(x, y, z);
            this.tempVec01.set(0, 0, size).add(x, y, z);
            this.tempNormal.set(0, -1, 0);
            this.addRect(tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.DOWN));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.UP)){
            this.tempVec00.set(size, size, 0).add(x, y, z);
            this.tempVec10.set(0, size, 0).add(x, y, z);
            this.tempVec11.set(0, size, size).add(x, y, z);
            this.tempVec01.set(size, size, size).add(x, y, z);
            this.tempNormal.set(0, 1, 0);
            this.addRect(tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.UP));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.WEST)){
            this.tempVec00.set(0, 0, 0).add(x, y, z);
            this.tempVec10.set(0, 0, size).add(x, y, z);
            this.tempVec11.set(0, size, size).add(x, y, z);
            this.tempVec01.set(0, size, 0).add(x, y, z);
            this.tempNormal.set(-1, 0, 0);
            this.addRect(tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.WEST));
        }

        if(tile.shouldShowFace(this.chunk.world, x, y, z, Direction.EAST)){
            this.tempVec00.set(size, 0, size).add(x, y, z);
            this.tempVec10.set(size, 0, 0).add(x, y, z);
            this.tempVec11.set(size, size, 0).add(x, y, z);
            this.tempVec01.set(size, size, size).add(x, y, z);
            this.tempNormal.set(1, 0, 0);
            this.addRect(tile.getTextureCoordsForSide(this.chunk.world, x, y, z, Direction.EAST));
        }
    }

    private void addRect(Vector2 texCoords){
        float u1 = TEX_TILE_SIZE*texCoords.x;
        float v1 = TEX_TILE_SIZE*texCoords.y;
        float u2 = TEX_TILE_SIZE*(texCoords.x+1);
        float v2 = TEX_TILE_SIZE*(texCoords.y+1);

        this.tempUv00.set(u1, v2);
        this.tempUv10.set(u2, v2);
        this.tempUv11.set(u2, v1);
        this.tempUv01.set(u1, v1);

        this.addTriangle(this.tempVec00, this.tempVec10, this.tempVec11, this.tempNormal, this.tempUv00, this.tempUv10, this.tempUv11);
        this.addTriangle(this.tempVec00, this.tempVec11, this.tempVec01, this.tempNormal, this.tempUv00, this.tempUv11, this.tempUv01);
    }

    private void addTriangle(Vector3 a, Vector3 b, Vector3 c, Vector3 normal, Vector2 uvA, Vector2 uvB, Vector2 uvC){
        this.addVertex(a, normal, uvA);
        this.addVertex(b, normal, uvB);
        this.addVertex(c, normal, uvC);
    }

    private void addVertex(Vector3 position, Vector3 normal, Vector2 uv){
        this.vertexCount++;

        this.vertices[this.floatIndex++] = position.x;
        this.vertices[this.floatIndex++] = position.y;
        this.vertices[this.floatIndex++] = position.z;

        this.vertices[this.floatIndex++] = uv.x;
        this.vertices[this.floatIndex++] = uv.y;

        this.vertices[this.floatIndex++] = normal.x;
        this.vertices[this.floatIndex++] = normal.y;
        this.vertices[this.floatIndex++] = normal.z;
    }

    public void render(ShaderProgram shader){
        if(this.mesh != null && this.vertexCount > 0){
            this.mesh.render(shader, GL20.GL_TRIANGLES, 0, this.vertexCount);
        }
    }

    @Override
    public void dispose(){
        if(this.mesh != null){
            this.mesh.dispose();
        }
    }
}