package io.github.amayaframework.router;

/**
 *
 */
public final class PathParameter extends Parameter {
    int index;

    /**
     * @param name
     * @param index
     * @param type
     */
    public PathParameter(String name, int index, String type) {
        super(name, type);
        this.index = index;
    }

    /**
     * @return
     */
    public int getIndex() {
        return index;
    }
}
