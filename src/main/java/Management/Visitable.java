package Management;

import Management.Visitor;

public interface Visitable {
    public void accept(Visitor visitor);
}
