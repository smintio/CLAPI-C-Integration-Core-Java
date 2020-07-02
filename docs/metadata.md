License metadata in Smint.io CLAPI-C
====================================

With CLAPI-C at Smint.io we use structured metadata objects to cluster
license information. Currently three objects are used:

* [License term](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncLicenseTerm.html)
* [Download constraints](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncDownloadConstraints.html)
* [Release details](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncReleaseDetails.html)
    (like model release, etc.)




[License term](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncLicenseTerm.html)
-------------------------------------------------------------------------------------------------------------------------------
License terms cluster various license attributes into a single instance.
Assets may refer to this instance instead of having to
replicate all license term attributes to their own property space.

License terms receives a UI visible name and a sequence number.
The sequence number is some sort of ID. Nevertheless it is not
globally unique but only within the same tenant.

License terms are never changed but somewhat read-only. When purchasing an asset
a snapshot of the current active license is attached to it. The purchase
is based on this license term. Hence it must not be changed afterwards.
In case new license terms become active, the previous one is marked as
inactive and a new license term instance is created.



### Storing license terms with target DAM

License terms contain quite a list of restrictions and allowances of usage
terms. It is possible to map all those attributes into asset space or copy the same
term attributes to all assets they apply to. Nevertheless it would be
best to store these data as a separate metadata instance and just refer
to it with the asset. Unfortunately, not all target DAM support such an
operation. In that case, it is acceptable to copy all license attributes
to each asset. Keep in mind to store the sequence number and the translated
license term name with each asset, too.




[Download constraints](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncDownloadConstraints.html)
-----------------------------------------------------------------------------------------------------------------------------------------------

This entity clusters download constraints, which are not very complicated.
Because of its simplicity, it can be easily mapped into asset property
space. It does not need to be kept as a separate instance.


[Release details](smintio-clapi-consumer-integration-core/1/io/smint/clapi/consumer/integration/core/target/ISyncReleaseDetails.html)
-------------------------------------------------------------------------------------------------------------------------------------

The <em>Asset Release Details</em> list whether an agreements have been made
with humans or properties visible on images or in movies/pictures. Under most
laws, models and and property owner of items or brand properties need to
agree to the release of the image or movie.
