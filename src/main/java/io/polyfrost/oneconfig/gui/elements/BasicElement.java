package io.polyfrost.oneconfig.gui.elements;

import io.polyfrost.oneconfig.lwjgl.RenderManager;
import io.polyfrost.oneconfig.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class BasicElement {
    protected int width, height;
    protected int colorPalette;
    protected int hitBoxX, hitBoxY;
    protected final boolean hoverFx;
    protected boolean hovered = false;
    protected boolean clicked = false;
    protected boolean toggled = false;
    protected int currentColor;

    public BasicElement(int width, int height, int colorPalette, boolean hoverFx) {
        this.height = height;
        this.width = width;
        this.colorPalette = colorPalette;
        this.hoverFx = hoverFx;
    }

    public BasicElement(int width, int height, boolean hoverFx) {
        this.height = height;
        this.width = width;
        this.colorPalette = -1;
        this.hoverFx = hoverFx;
    }


    public void draw(long vg, int x, int y) {
        RenderManager.drawRectangle(vg, x, y, width, height, currentColor);

        update(x, y);
        if (hoverFx) {
            currentColor = ColorUtils.getColor(currentColor, colorPalette, hovered, clicked);
        }
    }

    public void update(int x, int y) {
        int mouseX = Mouse.getX();
        int mouseY = Minecraft.getMinecraft().displayHeight - Math.abs(Mouse.getY());
        int buttonRight = x + width;
        int buttonBottom = y + height;

        hovered = mouseX > x - hitBoxX && mouseY > y - hitBoxY && mouseX < buttonRight + hitBoxX && mouseY < buttonBottom + hitBoxY;
        if (Mouse.isButtonDown(0) && clicked) {
            toggled = !toggled;
        }
        clicked = Mouse.isButtonDown(0) && hovered;
    }


    public void setCustomHitbox(int x, int y) {
        hitBoxX = x;
        hitBoxY = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setColorPalette(int colorPalette) {
        this.colorPalette = colorPalette;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isClicked() {
        return clicked;
    }

    public boolean isToggled() {
        return toggled;
    }

}
