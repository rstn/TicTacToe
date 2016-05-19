package com.simbirsoft.request;

import com.simbirsoft.model.Point;

public class PlayerMove {

    private int playerId;

    private Point point;

    public PlayerMove() {
    }

    public PlayerMove(int playerId, Point point) {
        this.playerId = playerId;
        this.point = point;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
