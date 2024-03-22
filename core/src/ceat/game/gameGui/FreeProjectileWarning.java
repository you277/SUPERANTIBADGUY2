package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Game;
import ceat.game.entity.FreeProjectile;
import ceat.game.entity.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class FreeProjectileWarning {
    private ArrayList<FreeProjectile> projectiles;
    private Player player;
    private BitmapFont font;
    public FreeProjectileWarning(Game game) {
        projectiles = game.getGrid().getFreeProjectiles();
        player = game.getPlayer();
        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 10;
                params.color = new Color(1, 0, 0, 1);
            }
        });
    }

    public void draw(SpriteBatch batch) {
        float closestDistance = Integer.MAX_VALUE;
        FreeProjectile closestProj;
        Vector2 playerPosition = player.getScreenPosition();
        for (FreeProjectile proj: projectiles) {
            Vector2 projPosition = proj.getPosition();
        }
    }
}
