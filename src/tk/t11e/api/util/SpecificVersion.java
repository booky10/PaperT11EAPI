package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (09:59 17.06.20)

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SpecificVersion {

    VersionHelper.Version from() default VersionHelper.Version.V1_8;

    VersionHelper.Version to() default VersionHelper.Version.V1_15;
}