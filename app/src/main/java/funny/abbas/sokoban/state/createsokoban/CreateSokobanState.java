package funny.abbas.sokoban.state.createsokoban;

import funny.abbas.sokoban.state.BaseStatus;

public enum CreateSokobanState implements BaseStatus {
    PUT_BASIC(1,"构建地图"),
    PUT_TARGET(2,"放置目标地块"),
    PUT_BOX(3,"放置箱子"),
    PUT_ROLE(4,"放置角色");
    public final int flag;
    public final String desc;

    CreateSokobanState(int flag, String desc) {
        this.flag = flag;
        this.desc = desc;
    }
}
