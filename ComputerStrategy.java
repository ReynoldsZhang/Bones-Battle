//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayList;
import java.util.Iterator;

public class ComputerStrategy implements Strategy {
    Player player;
    Territory attacker = null;
    Territory defender = null;
    ArrayList<TPair> targetPair = null;

    public ComputerStrategy() {
    }

    public void setPlayer(Player var1) {
        this.player = var1;
    }

    public boolean willAttack(Map var1) {
        this.targetPair = new ArrayList<>();
        ArrayList<Territory> var2 = var1.getPropertyOf(this.player);

        for (Territory var5 : var2) {
            ArrayList<Territory> var3 = var1.getEnemyNeighbors(var5);

            for (Territory var7 : var3) {
                if (var5.getDice() > 1 && var5.getDice() >= var7.getDice()) {
                    TPair var8 = new TPair();
                    var8.attacker = var5;
                    var8.defender = var7;
                    this.targetPair.add(var8);
                }
            }
        }

        return !this.targetPair.isEmpty();
    }

    public Territory getAttacker() {
        if (this.targetPair == null) {
            return null;
        } else {
            int var1 = (int)(Math.random() * (double)this.targetPair.size());
            return this.attacker = this.targetPair.get(var1).attacker;
        }
    }

    public Territory getDefender() {
        if (this.attacker != null) {

            for (TPair var2 : this.targetPair) {
                if (var2.attacker == this.attacker) {
                    return var2.defender;
                }
            }
        }

        return null;
    }

    private class TPair {
        public Territory attacker;
        public Territory defender;

        private TPair() {
        }
    }
}
