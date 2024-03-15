package ceat.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;

public class TexSprite extends Sprite {
    // cache textures
    private static final HashMap<String, Texture> textures = new HashMap<>();
    private static final HashMap<String, Integer> textureUsage = new HashMap<>();
    public static Texture getTexture(String path) {
        if (textures.containsKey(path)) {
            textureUsage.put(path, textureUsage.get(path) + 1);
            return textures.get(path);
        }
        Texture newTexture = new Texture(path);
        textures.put(path, newTexture);
        textureUsage.put(path, 1);
        return newTexture;
    }

    private final String texturePath;

    public TexSprite(String path) {
        super(getTexture(path));
        texturePath = path;
    }

    public void setCenter() {
        setCenter(getWidth()/2, getHeight()/2);
    }

    public void dispose() {
        int currentUsage = textureUsage.get(texturePath);
        if (currentUsage == 1) {
            textures.get(texturePath).dispose();
            textures.remove(texturePath);
            textureUsage.remove(texturePath);
            return;
        }
        textureUsage.put(texturePath, currentUsage - 1);
//        getTexture().dispose();
    }
}
