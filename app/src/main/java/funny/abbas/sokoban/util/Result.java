package funny.abbas.sokoban.util;

public class Result<T> {

    protected final T data;
    private Result(T data){
        this.data = data;
    }

    public boolean isSuccess(){
        return this instanceof Success;
    }

    public boolean isFailure(){
        return this instanceof Failure;
    }



    public static class Success<T> extends Result<T>{
        private Success(T data){
            super(data);
        }

        public T getData() {
            return data;
        }
    }

    public static class Failure<T> extends Result<T>{
        private final Exception e;
        private Failure(Exception e){
            super(null);
            this.e = e;
        }

        public Exception getE() {
            return e;
        }
    }
    public static <T> Result<T> success(T value){
        return new Success<>(value);
    }

    public static <T> Result<T> failure(Exception e){
        return new Failure<>(e);
    }
}
