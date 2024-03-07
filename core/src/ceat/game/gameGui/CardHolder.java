package ceat.game.gameGui;

import ceat.game.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class CardHolder {
    private Game game;
    private String[] cardNames = {"nothing", "bullet", "beam", "clear"};
    private ArrayList<AbilityCard> cards = new ArrayList<>();
    private AbilityCard currentOpenCard;
    private boolean hasOpenedCard;

    public CardHolder(Game game) {
        this.game = game;
        for (int i = 0; i < cardNames.length; i++) {
            cards.add(new AbilityCard(cardNames.length - i - 1, cardNames[i]));
        }
    }

    public void draw(SpriteBatch batch) {
        for (AbilityCard card: cards)
            card.draw(batch);
    }

    public void pullUp(int cardId) {
        if (hasOpenedCard) currentOpenCard.close();
        hasOpenedCard = true;
        currentOpenCard = cards.get(cardId);
        currentOpenCard.open();
    }

    public void closeAllCards() {
        if (hasOpenedCard) currentOpenCard.close();
        currentOpenCard = null;
        hasOpenedCard = false;
    }

    public void dispose() {
        for (AbilityCard card: cards)
            card.dispose();
    }
}
