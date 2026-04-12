/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Victor
 */

//Store a 3d array of blocks...
import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

//Texture imports
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;





public class Chunk {
private int VBOTextureHandle;
private Texture texture;
static final int CHUNK_SIZE = 30;
static final int CUBE_LENGTH = 2;

//3D ARRAY
private Block[][][] Blocks;
private int VBOVertexHandle;
private int VBOColorHandle;

private int StartX, StartY, StartZ;
private Random r;

//RENDER METHOD
public void render() {

    glPushMatrix();

    //glEnable(GL_TEXTURE_2D);
    //texture.bind(); // replaces glBindTexture

    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_TEXTURE_COORD_ARRAY);

    glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
    glVertexPointer(3, GL_FLOAT, 0, 0L);

    glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
    glTexCoordPointer(2, GL_FLOAT, 0, 0L);

    glDrawArrays(GL_QUADS, 0,
        CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);

    glPopMatrix();
}

    /**REBUILDMESH METHOD
     *
     * @param startX
     * @param startY
     * @param startZ
     * 
     */
public void rebuildMesh(float startX, float startY, float startZ) {

    VBOVertexHandle = glGenBuffers();
    VBOTextureHandle = glGenBuffers(); // Important

    FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer(
        (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12
    );

    FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer(
        (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 8 // fixed size
    );

    for (float x = 0; x < CHUNK_SIZE; x++) {
        for (float z = 0; z < CHUNK_SIZE; z++) {
            for (float y = 0; y < CHUNK_SIZE; y++) {

                VertexPositionData.put(createCube(
                    startX + x * CUBE_LENGTH,
                    y * CUBE_LENGTH,
                    startZ + z * CUBE_LENGTH
                ));

                VertexTextureData.put(
                    createCubeTexCoords(Blocks[(int)x][(int)y][(int)z])
                );
            }
        }
    }

    VertexPositionData.flip();
    VertexTextureData.flip(); // important

    // Upload vertices
    glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
    glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);

    // Upload textures
    glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
    glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);

    glBindBuffer(GL_ARRAY_BUFFER, 0);
} 



//cube Vertex Colors
private float[] createCubeVertexCol(float[] CubeColorArray) {
    float[] cubeColors = new float [CubeColorArray.length * 4 * 6];
    for (int i = 0; i < cubeColors.length; i++) {
        cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
    }  
    return cubeColors;
}

//create Cube Method
public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[] {
        // TOP QUAD
        x + offset, y + offset, z,
        x - offset, y + offset, z,
        x - offset, y + offset, z - CUBE_LENGTH,
        x + offset, y + offset, z - CUBE_LENGTH,
        // BOTTOM QUAD
        x + offset, y - offset, z - CUBE_LENGTH, 
        x - offset, y - offset, z - CUBE_LENGTH,
        x - offset, y - offset, z,
        x + offset, y - offset, z,
        // FRONT QUAD
        x + offset, y + offset, z - CUBE_LENGTH, 
        x - offset, y + offset, z - CUBE_LENGTH, 
        x - offset, y - offset, z - CUBE_LENGTH,
        x + offset, y - offset, z - CUBE_LENGTH,
        // BACK QUAD
        x + offset, y - offset, z, 
        x - offset, y - offset, z,
        x - offset, y + offset, z,
        x + offset, y + offset, z,
        // LEFT QUAD
        x - offset, y + offset, z - CUBE_LENGTH, 
        x - offset, y + offset, z, 
        x - offset, y - offset, z, 
        x - offset, y - offset, z - CUBE_LENGTH,
        // RIGHT QUAD
        x + offset, y + offset, z, 
        x + offset, y + offset, z - CUBE_LENGTH, 
        x + offset, y - offset, z - CUBE_LENGTH,
        x + offset, y - offset, z };
        }
        //SOLID COLORS
        private float[] getCubeColor(Block block) {
        switch (block.GetID()) {
            case 1:
                return new float[] {0, 1, 0};
            case 2:
                return new float[] { 1, 0.5f, 0 };
            case 3:
                return new float[] { 0, 0f, 1f };
            }
                return new float[] { 1, 1, 1 };
            }

private float[] createCubeTexCoords(Block block) {

    float u = 0f;
    float offset = 0.25f;

    switch (block.GetID()) {
        case 1: u = 0f; break;
        case 2: u = 0.25f; break;
        case 3: u = 0.5f; break;
        default: u = 0.75f; break;
    }

    return new float[] {
        u+offset,1,  u,1,  u,0,  u+offset,0,
        u+offset,1,  u,1,  u,0,  u+offset,0,
        u+offset,1,  u,1,  u,0,  u+offset,0,
        u+offset,1,  u,1,  u,0,  u+offset,0,
        u+offset,1,  u,1,  u,0,  u+offset,0,
        u+offset,1,  u,1,  u,0,  u+offset,0,
    };
}
        
//} Extra brace here messes up the code...

//CONSTRUCTOR, 3D GRID OF BLOCKS

public Chunk(int startX, int startY, int startZ){
    r = new Random();

    try {
        texture = TextureLoader.getTexture("PNG",
            ResourceLoader.getResourceAsStream("terrain.png"));
    } catch(Exception e) {
        e.printStackTrace();
    }

    Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

    for (int x = 0; x < CHUNK_SIZE; x++) {
        for (int y = 0; y < CHUNK_SIZE; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {

                float rand = r.nextFloat(); // 

                if(rand > 0.7f){
                    Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                } else if(rand > 0.4f){
                    Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                } else if(rand > 0.2f){
                    Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                } else {
                    Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                }
            }
        }
    }

    StartX = startX;
    StartY = startY;
    StartZ = startZ;

    rebuildMesh(startX, startY, startZ);
    }
}





    







