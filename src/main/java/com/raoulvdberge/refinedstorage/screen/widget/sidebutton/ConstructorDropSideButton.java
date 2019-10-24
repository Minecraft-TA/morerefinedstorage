package com.raoulvdberge.refinedstorage.screen.widget.sidebutton;

import com.raoulvdberge.refinedstorage.screen.BaseScreen;
import com.raoulvdberge.refinedstorage.tile.ConstructorTile;
import com.raoulvdberge.refinedstorage.tile.data.TileDataManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class ConstructorDropSideButton extends SideButton {
    public ConstructorDropSideButton(BaseScreen screen) {
        super(screen);
    }

    @Override
    protected void renderButtonIcon(int x, int y) {
        screen.blit(x, y, 64 + (ConstructorTile.DROP.getValue() ? 16 : 0), 16, 16, 16);
    }

    @Override
    public String getTooltip() {
        return I18n.format("sidebutton.refinedstorage.constructor.drop") + "\n" + TextFormatting.GRAY + I18n.format(ConstructorTile.DROP.getValue() ? "gui.yes" : "gui.no");
    }

    @Override
    public void onPress() {
        TileDataManager.setParameter(ConstructorTile.DROP, !ConstructorTile.DROP.getValue());
    }
}
