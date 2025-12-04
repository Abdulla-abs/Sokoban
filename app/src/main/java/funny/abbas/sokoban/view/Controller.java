package funny.abbas.sokoban.view;

public abstract class Controller {

    protected final Action action = new ActionImpl();

    protected abstract void onMoveLeft();

    protected abstract void onMoveTop();

    protected abstract void onMoveRight();

    protected abstract void onMoveBottom();

    private class ActionImpl implements Action {

        @Override
        public void moveLeft() {
            onMoveLeft();
        }

        @Override
        public void moveTop() {
            onMoveTop();
        }

        @Override
        public void moveRight() {
            onMoveRight();
        }

        @Override
        public void moveBottom() {
            onMoveBottom();
        }
    }
}
