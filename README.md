ALCC
====

Activity Lifecycle Callback Compatibility Library

Aims
====

With the Ice Cream Sandwich release of the Android API, a set of classes were added for observing Activity lifecycle callbacks.

These Activity lifecycle callback methods are a good jumping off point for managing an Activity from the outside, giving opportunities
to allow for smaller Activity class sizes and enabling things like dependency injection.

Disclaimer
==========

The implementation depends on dexmaker-1.1 (https://code.google.com/p/dexmaker/) to generate proxies that provide the ability to listen to Activity lifecycle events.
