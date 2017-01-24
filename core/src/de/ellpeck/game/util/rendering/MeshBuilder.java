package de.ellpeck.game.util.rendering;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import de.ellpeck.game.TheGame;

import java.util.ArrayList;
import java.util.List;

public class MeshBuilder implements Disposable{

    private static final int POSITION_COMPONENTS = 3;
    private static final int TEXTURE_COORDS = 2;
    private static final int NORMAL_COMPONENTS = 3;

    private final boolean hasTexCoords;
    private final boolean hasNormals;

    private Mesh mesh;

    private VertexAttributes vertexAttributes;
    private final FloatArray vertices = new FloatArray();
    private int vertexCount;

    private boolean isInit;

    public MeshBuilder(boolean hasNormals, boolean hasTexCoords){
        this.hasNormals = hasNormals;
        this.hasTexCoords = hasTexCoords;
    }

    public void init(){
        if(!this.isInit){
            this.vertices.clear();
            this.vertexCount = 0;

            if(this.mesh != null){
                this.mesh.dispose();
            }

            List<VertexAttribute> attributes = new ArrayList<>();
            attributes.add(new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"));
            if(this.hasTexCoords){
                attributes.add(new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, TEXTURE_COORDS, "a_textureCoords"));
            }
            if(this.hasNormals){
                attributes.add(new VertexAttribute(VertexAttributes.Usage.Normal, NORMAL_COMPONENTS, "a_normal"));
            }
            this.vertexAttributes = new VertexAttributes(attributes.toArray(new VertexAttribute[attributes.size()]));

            this.isInit = true;
        }
        else{
            throw new RuntimeException("Trying to initialize Mesh Builder "+this+" even though it is already initialized!");
        }
    }

    public void create(){
        if(this.isInit){
            this.mesh = new Mesh(true, this.vertexCount, 0, this.vertexAttributes);
            this.mesh.setVertices(this.vertices.shrink(), 0, this.vertices.size);

            this.isInit = false;

            TheGame.LOGGER.debug("Created mesh containing "+this.vertexCount+" vertices!");
        }
        else throw new RuntimeException("Trying to create Mesh from Mesh Builder "+this+" even though it was never initialized!");
    }

    public void addRectangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ, float dX, float dY, float dZ, float normalX, float normalY, float normalZ, TextureCoord uv){
        float u1 = uv.sizePercentage*uv.x;
        float v1 = uv.sizePercentage*uv.y;
        float u2 = uv.sizePercentage*(uv.x+1);
        float v2 = uv.sizePercentage*(uv.y+1);

        this.addRectangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, dX, dY, dZ, normalX, normalY, normalZ, u1, v1, u2, v1, u2, v2, u1, v2);
    }

    public void addRectangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ, float dX, float dY, float dZ){
        this.addRectangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, dX, dY, dZ, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F);
    }

    public void addRectangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ, float dX, float dY, float dZ, float uvAX, float uvAY, float uvBX, float uvBY, float uvCX, float uvCY, float uvDX, float uvDY){
        this.addRectangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, dX, dY, dZ, 0F, 0F, 0F, uvAX, uvAY, uvBX, uvBY, uvCX, uvCY, uvDX, uvDY);
    }

    public void addRectangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ, float dX, float dY, float dZ, float normalX, float normalY, float normalZ){
        this.addRectangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, dX, dY, dZ, normalX, normalY, normalZ, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F);
    }

    public void addRectangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ, float dX, float dY, float dZ, float normalX, float normalY, float normalZ, float uvAX, float uvAY, float uvBX, float uvBY, float uvCX, float uvCY, float uvDX, float uvDY){
        this.addTriangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, normalX, normalY, normalZ, uvAX, uvAY, uvBX, uvBY, uvCX, uvCY);
        this.addTriangle(aX, aY, aZ, cX, cY, cZ, dX, dY, dZ, normalX, normalY, normalZ, uvAX, uvAY, uvCX, uvCY, uvDX, uvDY);
    }

    public void addTriangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ){
        this.addTriangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F);
    }

    public void addTriangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ, float uvAX, float uvAY, float uvBX, float uvBY, float uvCX, float uvCY){
        this.addTriangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, 0F, 0F, 0F, uvAX, uvAY, uvBX, uvBY, uvCX, uvCY);
    }

    public void addTriangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ, float normalX, float normalY, float normalZ){
        this.addTriangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, normalX, normalY, normalZ, 0F, 0F, 0F, 0F, 0F, 0F);
    }

    public void addTriangle(float aX, float aY, float aZ, float bX, float bY, float bZ, float cX, float cY, float cZ, float normalX, float normalY, float normalZ, float uvAX, float uvAY, float uvBX, float uvBY, float uvCX, float uvCY){
        this.addVertex(aX, aY, aZ, normalX, normalY, normalZ, uvAX, uvAY);
        this.addVertex(bX, bY, bZ, normalX, normalY, normalZ, uvBX, uvBY);
        this.addVertex(cX, cY, cZ, normalX, normalY, normalZ, uvCX, uvCY);
    }

    public void addVertex(float x, float y, float z){
        this.addVertex(x, y, z, 0F, 0F, 0F, 0F, 0F);
    }

    public void addVertex(float x, float y, float z, float u, float v){
        this.addVertex(x, y, z, 0F, 0F, 0F, u, v);
    }

    public void addVertex(float x, float y, float z, float normalX, float normalY, float normalZ){
        this.addVertex(x, y, z, normalX, normalY, normalZ, 0F, 0F);
    }

    public void addVertex(float x, float y, float z, float normalX, float normalY, float normalZ, float u, float v){
        this.vertexCount++;

        this.vertices.add(x);
        this.vertices.add(y);
        this.vertices.add(z);

        this.addUv(u, v);
        this.addNormal(normalX, normalY, normalZ);
    }

    public void addUv(float u, float v){
        if(this.hasTexCoords){
            this.vertices.add(u);
            this.vertices.add(v);
        }
        else{
            throw new RuntimeException("Tried to add UV "+u+", "+v+" to Mesh Builder "+this+" that doesn't allow texture coordinates!");
        }
    }

    public void addNormal(float x, float y, float z){
        if(this.hasNormals){
            this.vertices.add(x);
            this.vertices.add(y);
            this.vertices.add(z);
        }
        else{
            throw new RuntimeException("Tried to add Normal "+x+", "+y+", "+z+" to Mesh Builder "+this+" that doesn't allow normals!");
        }
    }

    public void renderMesh(ShaderProgram shader){
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
