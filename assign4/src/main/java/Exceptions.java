class NotImplementedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Please remove this line of code (throw new NotImplementedException()) and implement the function";
    }
}

class ConflictedTimeslotException extends IllegalArgumentException {
}

class InvalidGradeException extends IllegalArgumentException {
}

class CourseNotTakenException extends IllegalArgumentException {
}

class CourseAlreadyPassedException extends IllegalArgumentException {
}

class CourseAlreadyEnrolledException extends IllegalArgumentException {
}

class NoLeftCapacityException extends Exception {
}
