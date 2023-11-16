public class Manager {

    private String name;
    private ArrayList<User> staff;

    public Manager(String name) {

        this.name = name;
        this.staff = new ArrayList<User>();
    }

    public void appointStaff(User user) {

        this.staff.add(user);
    }

    public void removeStaff(User user) {

        this.staff.remove(user);
    }
}
