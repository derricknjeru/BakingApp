package com.derrick.bakingapp;

// Runs all unit tests.

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({MainActivityTest.class,
        DetailsActivityTest.class})
public class BakingAppUnitTextSuite { }
