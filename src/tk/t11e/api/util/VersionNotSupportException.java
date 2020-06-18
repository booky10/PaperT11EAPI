package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (19:59 15.06.20)

public class VersionNotSupportException extends IllegalStateException {

    public VersionNotSupportException() {
        super();
    }

    public VersionNotSupportException(VersionHelper.Version version) {
        super("Version " + version.name() + " is currently not supported in this operation!");
    }

    public VersionNotSupportException(String message) {
        super(message);
    }

    public VersionNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionNotSupportException(Throwable cause) {
        super(cause);
    }

    static final long serialVersionUID = -56162363625743296L;
}