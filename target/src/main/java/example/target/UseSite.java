package example.target;

import example.base.AnnotatedClassBase;
import example.base.LookAt;

public class UseSite {
    @LookAt
    AnnotatedClassTarget annotatedClassTarget;
    @LookAt
    AnnotatedClassBase annotatedClassBase;

    public static void main(String[] args) {
        System.out.println("bla");
    }
}
