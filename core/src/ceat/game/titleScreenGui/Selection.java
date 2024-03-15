package ceat.game.titleScreenGui;

import ceat.game.Font;
import ceat.game.Lerp;
import ceat.game.fx.SelectionParticles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Selection {
    private final String text;

    private float baseX;
    private float baseY;
    private float offsetX;
    private float offsetY;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private boolean selected;
    private float lifetime;

    public Selection(String text) {
        this.text = text;
        font = Font.create(new Font.ParamSetter() {
            public void run(FreeTypeFontGenerator.FreeTypeFontParameter params) {
                params.size = 20;
            }
        });
        layout = new GlyphLayout();
        layout.setText(font, text);
    }

    public Selection setPosition(float x, float y) {
        baseX = x;
        baseY = y;
        return this;
    }

    public Selection setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
    private float lastParticleEmitTime;

    public void draw(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        lifetime += delta;

        if (lifetime - lastParticleEmitTime > 0.05 && selected) {
            lastParticleEmitTime = lifetime;
            for (int i = 0; i < 3; i++)
                new SelectionParticles(0, baseY - (float)Math.random()*layout.height).play();
        }

        float targetXOffset = selected ? 50 : 0;
        offsetX = Lerp.lerp(offsetX, targetXOffset, Lerp.alpha(delta, 10));
        offsetY = (float)-Math.sin(lifetime*1.5f)*3;

        font.draw(batch, text, baseX + offsetX + 30, baseY + offsetY);
    }

    public void dispose() {
        font.dispose();
    }
}
