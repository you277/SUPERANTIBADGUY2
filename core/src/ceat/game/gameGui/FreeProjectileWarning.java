package ceat.game.gameGui;

import ceat.game.Font;
import ceat.game.Game;
import ceat.game.GameHandler;
import ceat.game.entity.FreeProjectile;
import ceat.game.entity.Player;
import ceat.game.screen.ScreenOffset;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class FreeProjectileWarning {
    private final ArrayList<FreeProjectile> projectiles;
    private final Player player;
    private final BitmapFont font;
    private final BitmapFont font2;
    private float cycle;
    public FreeProjectileWarning(Game game) {
        projectiles = game.getGrid().getFreeProjectiles();
        player = game.getPlayer();
        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 20;
                params.color = new Color(1, 0, 0, 1);
            }
        });
        font2 = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 20;
                params.color = new Color(1, 1, 1, 1);
            }
        });
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public void draw(SpriteBatch batch) {
        float delta = GameHandler.getDeltaTime();
        cycle = (cycle + delta)%0.4f;
        if (!player.getIsAlive()) return;

        float closestDistance = Integer.MAX_VALUE;
        float greatestDistanceClosed = Integer.MIN_VALUE;

        FreeProjectile greatestClosed = null;

        Vector2 playerPosition = player.getScreenPosition();

        for (FreeProjectile proj: projectiles) {
            Vector2 projPosition = proj.getPosition();
            float distance = getDistance(playerPosition.x, playerPosition.y, projPosition.x, projPosition.y);
            if (distance < closestDistance) {
                closestDistance = distance;
            }
            float distance2 = getDistance(playerPosition.x, playerPosition.y, projPosition.x + proj.getXVelocity()*delta, projPosition.y + proj.getYVelocity()*delta);
            float closingDistance = distance - distance2;
            if (closingDistance > greatestDistanceClosed) {
                greatestDistanceClosed = closingDistance;
                greatestClosed = proj;
            }
        }
        if (closestDistance > 50) return;
        if (greatestDistanceClosed < 0) return;

        double angle = Math.atan2(greatestClosed.getPosition().y - playerPosition.y, greatestClosed.getPosition().x - playerPosition.x);

        float x = (float)Math.cos(angle);
        float y = (float)Math.sin(angle);

        Vector2 position = ScreenOffset.project(
                playerPosition.x + x*20 - Font.getTextWidth(font, "!")/2 + player.getSprite().getHeight()/2,
                playerPosition.y + y*20 + Font.getTextHeight(font, "!")/2 + player.getSprite().getHeight()/2
        );

        (cycle < 0.2f ? font : font2).draw(batch, "!", position.x, position.y);
    }

   public void dispose() {
        font.dispose();
        font2.dispose();
   }

   public String toString() {
        return "FREE PROJECTILE WARNING";
   }
   public boolean equals() {
        return false;
   }
}
