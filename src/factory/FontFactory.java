package factory;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Font;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.Effect;

public class FontFactory {

    private static FontFactory factory;
    Map<String, Map<Integer, Map<Effect, Font>>> typeSizeEffectFont;

    private FontFactory() {
        this.typeSizeEffectFont = new HashMap<String, Map<Integer, Map<Effect, Font>>>();
    }

    public static FontFactory getInstance() {
        if (FontFactory.factory == null) {
            FontFactory.factory = new FontFactory();
        }
        return FontFactory.factory;
    }

    @SuppressWarnings("unchecked")
    public Font getFont(String type, int size, Effect effect) throws SlickException {
        if (!this.typeSizeEffectFont.containsKey(type)) {
            this.typeSizeEffectFont.put(type, new HashMap<Integer, Map<Effect, Font>>());
        }
        if (!this.typeSizeEffectFont.get(type).containsKey(size)) {
            this.typeSizeEffectFont.get(type).put(size, new HashMap<Effect, Font>());
        }
        if (!this.typeSizeEffectFont.get(type).get(size).containsKey(effect)) {
            UnicodeFont font = new UnicodeFont(String.format("content/fonts/%s.ttf", type), size,
                    false, false);
            font.addGlyphs(32, 382);
            font.getEffects().add(effect);
            font.loadGlyphs();
            this.typeSizeEffectFont.get(type).get(size).put(effect, font);
        }
        return this.typeSizeEffectFont.get(type).get(size).get(effect);
    }
}
