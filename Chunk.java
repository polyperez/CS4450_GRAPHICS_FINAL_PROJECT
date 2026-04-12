import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk {
    private int VBOTextureHandle;
    private Texture texture;

    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;

    private Block[][][] Blocks;
    private int VBOVertexHandle;

    private int StartX, StartY, StartZ;
    private Random r;

    public Chunk(int startX, int startY, int startZ) {
        r = new Random();

        try {
            texture = TextureLoader.getTexture("PNG",
                    ResourceLoader.getResourceAsStream("terrain.png"));
            System.out.println("terrain.png loaded successfully");
        } catch (Exception e) {
            System.out.println("Could not load terrain.png");
            e.printStackTrace();
        }

        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    float rand = r.nextFloat();

                    if (rand > 0.7f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    } else if (rand > 0.4f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    } else if (rand > 0.2f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    } else {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                    }

                    Blocks[x][y][z].setActive(true);
                }
            }
        }

        StartX = startX;
        StartY = startY;
        StartZ = startZ;

        rebuildMesh(startX, startY, startZ);
    }

    public void render() {
        glPushMatrix();

        glEnable(GL_TEXTURE_2D);

        if (texture != null) {
            texture.bind();
        }

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glTexCoordPointer(2, GL_FLOAT, 0, 0L);

        glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);

        glPopMatrix();
    }

    public void rebuildMesh(float startX, float startY, float startZ) {
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();

        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer(
                (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);

        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer(
                (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 8);

        for (float x = 0; x < CHUNK_SIZE; x++) {
            for (float z = 0; z < CHUNK_SIZE; z++) {
                for (float y = 0; y < CHUNK_SIZE; y++) {
                    VertexPositionData.put(createCube(
                            startX + x * CUBE_LENGTH,
                            startY + y * CUBE_LENGTH,
                            startZ + z * CUBE_LENGTH));

                    VertexTextureData.put(
                            createCubeTexCoords(Blocks[(int) x][(int) y][(int) z]));
                }
            }
        }

        VertexPositionData.flip();
        VertexTextureData.flip();

        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;

        return new float[] {
            // TOP
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,

            // BOTTOM
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,

            // FRONT
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,

            // BACK
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,

            // LEFT
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,

            // RIGHT
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z
        };
    }

    private float[] createCubeTexCoords(Block block) {
        float tileSize = 1.0f / 16.0f;

        int topCol = 0, topRow = 0;
        int bottomCol = 0, bottomRow = 0;
        int sideCol = 0, sideRow = 0;

        switch (block.GetID()) {
            case 0: // Grass
                // top-left tile you pointed out is grass top
                // dirt = (2,0)
                // grass side = (3,0)
                topCol = 0;    topRow = 0;
                bottomCol = 2; bottomRow = 0;
                sideCol = 3;   sideRow = 0;
                break;

            case 1: // Sand
                topCol = bottomCol = sideCol = 1;
                topRow = bottomRow = sideRow = 1;
                break;

            case 2: // Water
                topCol = bottomCol = sideCol = 13;
                topRow = bottomRow = sideRow = 12;
                break;

            case 3: // Dirt
                topCol = bottomCol = sideCol = 2;
                topRow = bottomRow = sideRow = 0;
                break;

            case 4: // Stone
                topCol = bottomCol = sideCol = 1;
                topRow = bottomRow = sideRow = 0;
                break;

            case 5: // Bedrock
                topCol = bottomCol = sideCol = 1;
                topRow = bottomRow = sideRow = 1;
                break;

            default:
                topCol = bottomCol = sideCol = 0;
                topRow = bottomRow = sideRow = 0;
                break;
        }

        float topU = topCol * tileSize;
        float topV = topRow * tileSize;
        float topU2 = topU + tileSize;
        float topV2 = topV + tileSize;

        float bottomU = bottomCol * tileSize;
        float bottomV = bottomRow * tileSize;
        float bottomU2 = bottomU + tileSize;
        float bottomV2 = bottomV + tileSize;

        float sideU = sideCol * tileSize;
        float sideV = sideRow * tileSize;
        float sideU2 = sideU + tileSize;
        float sideV2 = sideV + tileSize;

        return new float[] {
            // TOP
            topU2, topV,   topU, topV,   topU, topV2,   topU2, topV2,

            // BOTTOM
            bottomU2, bottomV,   bottomU, bottomV,   bottomU, bottomV2,   bottomU2, bottomV2,

            // FRONT
            sideU2, sideV,   sideU, sideV,   sideU, sideV2,   sideU2, sideV2,

            // BACK
            sideU2, sideV,   sideU, sideV,   sideU, sideV2,   sideU2, sideV2,

            // LEFT
            sideU2, sideV,   sideU, sideV,   sideU, sideV2,   sideU2, sideV2,

            // RIGHT
            sideU2, sideV,   sideU, sideV,   sideU, sideV2,   sideU2, sideV2
        };
    }
}