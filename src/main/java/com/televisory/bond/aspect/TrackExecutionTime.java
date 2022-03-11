package com.televisory.bond.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TrackExecutionTime {
	
	
	
	/** This interface annotation when used on any method will cause the track of its execution time*/

}
