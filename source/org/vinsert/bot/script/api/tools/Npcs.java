package org.vinsert.bot.script.api.tools;

import org.vinsert.bot.script.ScriptContext;
import org.vinsert.bot.script.api.Npc;
import org.vinsert.bot.script.api.Tile;
import org.vinsert.bot.util.Filter;
import org.vinsert.bot.util.Perspective;
import org.vinsert.insertion.INpc;

import java.util.ArrayList;
import java.util.List;


/**
 * Npc methods
 *
 * @author tommo
 * @author `Discardedx2
 */
public class Npcs {

    private ScriptContext ctx;

    public Npcs(ScriptContext ctx) {
        this.ctx = ctx;
    }

    /**
     * @return The list of nearby local npcs
     */
    public List<Npc> getAll() {
        return getNpcs(null);
    }


    /**
     * Gets a filtered list of npcs.
     *
     * @param filter The filter.
     * @return The npcs.
     */
    public List<Npc> getNpcs(Filter<Npc> filter) {
        final List<Npc> npcs = new ArrayList<Npc>();
        final INpc[] rsnpcs = ctx.getClient().getNpcs();
        final int[] npcIndices = ctx.getClient().getNpcIndices();
        for (int i = 0; i < rsnpcs.length; i++) {
            INpc npc = rsnpcs[npcIndices[i]];
            if (npc != null) {
                Npc rsnpc = new Npc(ctx, npc);

                if (filter == null || filter.accept(rsnpc)) {
                    npcs.add(rsnpc);
                }
            }
        }
        return npcs;
    }

    /**
     * Gets the nearest npc.
     *
     * @param src Source position.
     * @return The nearest npc.
     */
    public Npc getNearest(Tile src) {
        List<Npc> npcs = getAll();

        if (npcs.isEmpty()) {
            return null;
        }

        Npc closest = npcs.get(0);
        for (Npc npc : npcs) {
            if (Perspective.edist_tile(src, npc.getLocation()) < Perspective.edist_tile(src, closest.getLocation())) {
                closest = npc;
            }
        }
        return closest;
    }

    /**
     * Returns the nearest npc with one of the given ids
     *
     * @param ids The valid ids
     * @return The nearest npc, may be null
     */
    public Npc getNearest(final int... ids) {
        return getNearest(ctx.players.getLocalPlayer().getLocation(), new Filter<Npc>() {
            public boolean accept(Npc npc) {
                for (int id : ids) {
                    if (npc.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Applies a filter and gets the nearest npc.
     *
     * @param src    Source position.
     * @param filter Filter to apply.
     * @return The nearest npc.
     */
    public Npc getNearest(Tile src, Filter<Npc> filter) {
        List<Npc> npcs = getNpcs(filter);

        if (npcs.isEmpty()) {
            return null;
        }

        Npc closest = npcs.get(0);
        for (Npc npc : npcs) {
            if (Perspective.edist_tile(src, npc.getLocation()) < Perspective.edist_tile(src, closest.getLocation())) {
                closest = npc;
            }
        }
        return closest;
    }

}
