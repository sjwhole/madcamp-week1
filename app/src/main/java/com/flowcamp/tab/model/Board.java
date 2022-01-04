package com.flowcamp.tab.model;


import android.util.Log;

public class Board {

    private Cell[][] cells = new Cell[3][3];

    private Player winner;      // 승리한 사람
    private Player currentTurn; // 현재 차례 플레이어
    private GameState state;    // 게임 진행 상태

    private enum GameState {IN_PROGRESS, FINISHED}

    public Board() {
        restart();
    }

    public void restart() {
        clearCells();       // 모든 셀 초기화
        winner = null;
        currentTurn = Player.X;     // X부터 시작
        state = GameState.IN_PROGRESS;
    }

    private void clearCells() {     // 모든 셀 초기화
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public Player mark(int row, int col, Player player) {

        Player playerThatMoved = null;      // 움직인 선수 초기화

        if (isValid(row, col)) {     // 해당 셀을 선택할 수 있으면

            cells[row][col].setValue(player);  // 해당 셀에 현재 차례의 선수 삽입
            playerThatMoved = player;      // 현재 차례 선수를 움직인 선수에 삽입

            if (isWinningMoveByPlayer(player, row, col)) {      // 승부가 났는지 확인
                state = GameState.FINISHED;
                winner = player;

            }
//            else {
//                // flip the current turn and continue
//                flipCurrentTurn();      // 현재 차례 선수 바꿈.
//            }
        }

        return playerThatMoved;
    }

    public Player getWinner() {
        return winner;
    }

    private boolean isValid(int row, int col) {        // 해당 셀이 선택 가능한지 여부
        if (state == GameState.FINISHED) {
            return false;
        } else if (isOutOfBounds(row) || isOutOfBounds(col)) {
            return false;
        } else if (isCellValueAlreadySet(row, col)) {      // 이미 선택된 셀이면
            return false;
        } else {
            return true;
        }
    }

    private boolean isOutOfBounds(int idx) {
        return idx < 0 || idx > 2;
    }

    private boolean isCellValueAlreadySet(int row, int col) {
        return cells[row][col].getValue() != null;
    }

    private boolean isWinningMoveByPlayer(Player player, int currentRow, int currentCol) {

        return (cells[currentRow][0].getValue() == player         // 3-in-the-row
                && cells[currentRow][1].getValue() == player
                && cells[currentRow][2].getValue() == player
                || cells[0][currentCol].getValue() == player      // 3-in-the-column
                && cells[1][currentCol].getValue() == player
                && cells[2][currentCol].getValue() == player
                || currentRow == currentCol            // 3-in-the-diagonal
                && cells[0][0].getValue() == player
                && cells[1][1].getValue() == player
                && cells[2][2].getValue() == player
                || currentRow + currentCol == 2    // 3-in-the-opposite-diagonal
                && cells[0][2].getValue() == player
                && cells[1][1].getValue() == player
                && cells[2][0].getValue() == player);
    }

    private boolean isPlayerWinner(Player player) {
        return (cells[0][0].getValue() == player         // 3-in-the-row
                && cells[0][1].getValue() == player
                && cells[0][2].getValue() == player)
                || (cells[1][0].getValue() == player      // 3-in-the-column
                && cells[1][1].getValue() == player
                && cells[1][2].getValue() == player)
                || (cells[2][0].getValue() == player
                && cells[2][1].getValue() == player
                && cells[2][2].getValue() == player)
                || (cells[0][0].getValue() == player
                && cells[1][0].getValue() == player
                && cells[2][0].getValue() == player)
                || (cells[0][1].getValue() == player
                && cells[1][1].getValue() == player
                && cells[2][1].getValue() == player)
                || (cells[0][2].getValue() == player
                && cells[1][2].getValue() == player
                && cells[2][2].getValue() == player)
                || (cells[0][0].getValue() == player
                && cells[1][1].getValue() == player
                && cells[2][2].getValue() == player)
                || (cells[0][2].getValue() == player
                && cells[1][1].getValue() == player
                && cells[2][0].getValue() == player);
    }

    private boolean isGameOver() {
        return isPlayerWinner(Player.X) || isPlayerWinner(Player.O);
    }


    private void flipCurrentTurn() {
        currentTurn = currentTurn == Player.X ? Player.O : Player.X;
    }


    private int evaluate() {
        if (isPlayerWinner(Player.X)) {
            return 1;
        } else if (isPlayerWinner(Player.O)) {
            return -1;
        }
        return 0;
    }

    private Choice minimax(Cell[][] state, int depth, Player player) {
        Choice choice, cur;
        if (player == Player.X) {
            choice = new Choice(-1, -1, -1000000);
        } else {
            choice = new Choice(-1, -1, 1000000);
        }
        if (depth == 0 || isGameOver()) {
            return new Choice(-1, -1, evaluate());
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isCellValueAlreadySet(i, j)) continue;
                state[i][j].setValue(player);
                cur = minimax(state, depth - 1, player == Player.X ? Player.O : Player.X);
                Log.i("i", Integer.toString(i) + j);
                state[i][j].setValue(null);
                cur.setRow(i);
                cur.setColumn(j);

                if (player == Player.X) {
                    if (cur.getScore() > choice.getScore()) {
                        choice = cur;
                    }
                } else {
                    if (cur.getScore() < choice.getScore()) {
                        choice = cur;
                    }
                }
            }
        }
        return choice;
    }

    public Choice com_choice() {
        int depth = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j].getValue() == null) {
                    depth++;
                }
            }
        }
        if (depth == 0 || isGameOver()) return null;
        return minimax(cells, depth, Player.X);
//        cells[choice.getRow()][choice.getColumn()].setValue(Player.X);
    }
}

