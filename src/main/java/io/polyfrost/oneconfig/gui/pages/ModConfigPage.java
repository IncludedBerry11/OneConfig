package io.polyfrost.oneconfig.gui.pages;

import io.polyfrost.oneconfig.config.OneConfigConfig;
import io.polyfrost.oneconfig.config.data.Mod;
import io.polyfrost.oneconfig.config.data.OptionPage;
import io.polyfrost.oneconfig.config.interfaces.BasicOption;
import io.polyfrost.oneconfig.lwjgl.RenderManager;
import io.polyfrost.oneconfig.lwjgl.font.Fonts;

public class ModConfigPage extends Page {
    private final OptionPage page;

    public ModConfigPage(OptionPage page) {
        super("Mod: " + page.mod.name);
        this.page = page;
    }

    @Override
    public void draw(long vg, int x, int y) {
        if (page.categories.size() == 0) return;
        String selectedCategory = page.categories.keySet().stream().findFirst().get();
        int optionX = x + 16;
        int optionY = y + (page.categories.size() == 1 ? 32 : 72);
        for (String subCategory : page.categories.get(selectedCategory).keySet()) {
            RenderManager.drawString(vg, subCategory, x + 18, optionY, OneConfigConfig.WHITE, 24f, Fonts.INTER_MEDIUM);
            optionY += 20;

            int backgroundSize = 32;
            for (int i = 0; i < page.categories.get(selectedCategory).get(subCategory).size(); i++) {
                BasicOption option = page.categories.get(selectedCategory).get(subCategory).get(i);
                if (i + 1 < page.categories.get(selectedCategory).get(subCategory).size()) {
                    BasicOption nextOption = page.categories.get(selectedCategory).get(subCategory).get(i + 1);
                    if (option.size == 1 && option.hasHalfSize() && nextOption.size == 1 && nextOption.hasHalfSize()) {
                        backgroundSize += Math.max(option.getHeight(), nextOption.getHeight()) + 16;
                        i++;
                        continue;
                    }
                }
                backgroundSize += option.getHeight();
            }
            RenderManager.drawRoundedRect(vg, x + 14, optionY, 1024, backgroundSize, OneConfigConfig.GRAY_900, 20);

            optionY += 16;
            for (int i = 0; i < page.categories.get(selectedCategory).get(subCategory).size(); i++) {
                BasicOption option = page.categories.get(selectedCategory).get(subCategory).get(i);
                option.draw(vg, optionX, optionY);
                if (i + 1 < page.categories.get(selectedCategory).get(subCategory).size()) {
                    BasicOption nextOption = page.categories.get(selectedCategory).get(subCategory).get(i + 1);
                    if (option.size == 1 && option.hasHalfSize() && nextOption.size == 1 && nextOption.hasHalfSize()) {
                        nextOption.draw(vg, optionX + 464, optionY);
                        optionY += Math.max(option.getHeight(), nextOption.getHeight()) + 16;
                        i++;
                        continue;
                    }
                }
                optionY += option.getHeight() + 16;
            }
        }
    }

    @Override
    public void finishUpAndClose() {
        page.mod.config.save();      // TODO
    }
}