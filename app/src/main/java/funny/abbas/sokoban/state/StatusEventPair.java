package funny.abbas.sokoban.state;


import com.google.common.base.Objects;

/**
 * 状态事件对，指定的状态只能接受指定的事件
 */
public class StatusEventPair<S extends BaseStatus, E extends BaseEvent> {
    /**
     * 指定的状态
     */
    private final S status;
    /**
     * 可接受的事件
     */
    private final E event;

    public StatusEventPair(S status, E event) {
        this.status = status;
        this.event = event;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StatusEventPair<?, ?> that = (StatusEventPair<?, ?>) obj;
        return java.util.Objects.equals(status, that.status) && java.util.Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        // 这里使用的是google的guava包。com.google.common.base.Objects
        return Objects.hashCode(status, event);
    }
}
