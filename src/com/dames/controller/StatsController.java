package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Database;
import model.Player;
import view.StatsView;
import java.util.Map;
import java.util.List;

public class StatsController {
    private final StatsView view;
    private final Database db;
    private final Player player;

    public StatsController(StatsView view, Database db, Player player) {
        this.view = view;
        this.db = db;
        this.player = player;
        
        // TODO: Charger les statistiques du joueur depuis la base de données
        view.setPlayerName(player.getUsername());
        view.setGamesPlayed(0);
        view.setWins(0);
        view.setLosses(0);
        view.setDraws(0);
        
        initializeView();
    }

    private void initializeView() {
        view.addCloseListener(e -> view.dispose());
    }

    public Player getPlayer() {
        return player;
    }

    public Database getDb() {
        return db;
    }

    public void showPlayerStats(String playerId) {
        Map<String, Object> stats = db.getPlayerStats(playerId);
        view.updatePlayerStats(stats);
        
        List<Map<String, Object>> rankings = db.getGlobalRankings();
        view.updateRankings(rankings);
        
        view.setVisible(true);
    }

    public void updateGameStats(String playerId, boolean isWin, boolean isDraw,
                              int movesCount, int captures, int promotions,
                              long gameDuration) {
        db.updateGameStats(playerId, isWin, isDraw, movesCount,
                               captures, promotions, gameDuration);
    }

    class CloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }
}