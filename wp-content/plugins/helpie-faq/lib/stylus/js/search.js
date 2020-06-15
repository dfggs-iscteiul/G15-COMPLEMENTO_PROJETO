var Stylus = {
    //setup before functions
    searchTerm: "",
    typingTimer: 0, // timer identifier
    doneTypingInterval: 2000, // time in ms, 2 second for example
    showNoFAQsLabel: false,
    accordionHeader: ".accordion .accordion__item .accordion__header",

    setSearchAttr: function() {
        /* Add 'search_attr' to accordion headers */
        jQuery(this.accordionHeader).each(function() {
            var search_attr = jQuery(this)
                .text()
                .toLowerCase();

            if (
                jQuery(this)
                    .closest(".accordion")
                    .closest(".accordion__body")
                    .closest(".accordion__item").length > 0
            ) {
                search_attr +=
                    " " +
                    jQuery(this)
                        .closest(".accordion")
                        .closest(".accordion__body")
                        .closest(".accordion__item")
                        .find(".accordion__header")
                        .first()
                        .text()
                        .toLowerCase();
            }

            jQuery(this).attr("data-search-term", search_attr);
        });

        /* Add 'search_attr' to accordion section title ( category name ) */
        jQuery(".accordions .accordion__section h3").each(function() {
            var search_attr = jQuery(this)
                .text()
                .toLowerCase();
            jQuery(this).attr("data-search-term", search_attr);
        });
    },

    isContentMatch: function(element, searchTerm) {
        var content = jQuery(element)
            .closest(".accordion__item")
            .find(".accordion__body")
            .text()
            .toLowerCase();
        // $is_match = content.match(searchTerm);
        if (content.indexOf(searchTerm) >= 0) return true;

        return false;
        // return $is_match;
    },

    isTitleMatch: function(element, searchTerm) {
        var filterMatch =
            jQuery(element).filter("[data-search-term *= " + searchTerm + "]")
                .length > 0;
        var TitleMatch = filterMatch || searchTerm.length < 1 ? true : false;

        return TitleMatch;
    },

    onSearchKeyup: function(that) {
        var thisModule = this;

        var matches = 0;

        const searchTerm = jQuery(that)
            .val()
            .toLowerCase();
        thisModule.searchTerm = jQuery(that)
            .val()
            .toLowerCase();

        /* Loop through each accordion item header */
        jQuery(that)
            .closest(".accordions")
            .find(thisModule.accordionHeader)
            .each(function() {
                var TitleMatch = thisModule.isTitleMatch(this, searchTerm);
                var ContentMatch = thisModule.isContentMatch(this, searchTerm);

                var show = TitleMatch || ContentMatch;
                if (show == true) {
                    jQuery(this)
                        .closest(".accordion__item")
                        .show();

                    var accordionItem = jQuery(this)
                        .closest(".accordion__item")
                        .closest(".accordion__body")
                        .closest(".accordion__item");
                    if (accordionItem.length > 0) {
                        accordionItem.show();
                    }
                    matches++;
                } else {
                    jQuery(this)
                        .closest(".accordion__item")
                        .hide();
                }
            });

        /* Feature Starts -- Show NO FAQs when search is empty */
        if (thisModule.showNoFAQsLabel == false) {
            jQuery(that)
                .closest(".accordions")
                .append("<span class='no-faqs'>No FAQs found.</span>");
            thisModule.showNoFAQsLabel = true;
        }

        if (matches != 0) {
            jQuery(".no-faqs").hide();
        }

        if (matches == 0) {
            jQuery(".no-faqs").show();
        }

        /* -- Feature Ends */
        /* Hide Category Title ( h3 ) */

        if (searchTerm.length < 1) {
            jQuery(that)
                .closest(".helpie-faq.accordions")
                .find(".accordion__section")
                .show();
            return;
        }

        console.log("matches: " + matches);
        console.log("showNoFAQsLabel: " + thisModule.showNoFAQsLabel);

        if (
            jQuery(that)
                .closest(".accordions")
                .has(".accordion__section")
        ) {
            jQuery(that)
                .closest(".accordions")
                .find(".accordion")
                .each(function() {
                    var showOne = false;

                    jQuery(this)
                        .find(".accordion__item")
                        .each(function() {
                            var TitleMatch = thisModule.isTitleMatch(
                                this,
                                searchTerm
                            );
                            var ContentMatch = thisModule.isContentMatch(
                                this,
                                searchTerm
                            );

                            var show = TitleMatch || ContentMatch;
                            showOne = showOne || show;
                        });

                    if (showOne == false) {
                        jQuery(this)
                            .closest(".accordion__section")
                            .hide();
                    } else {
                        jQuery(this)
                            .closest(".accordion__section")
                            .show();
                    }

                    /* Show entire Matched category */
                    var filterMatch =
                        jQuery(this)
                            .closest(".accordion__section")
                            .find("h3")
                            .filter(
                                '[data-search-term *= "' + searchTerm + '"]'
                            ).length > 0;
                    var showCategory =
                        filterMatch || searchTerm.length < 1 ? true : false;

                    if (showCategory == true) {
                        jQuery(this)
                            .closest(".accordion__section")
                            .show();
                        jQuery(this)
                            .closest(".accordion__section")
                            .find(".accordion .accordion__item")
                            .show();
                    }
                });

            return searchTerm;
        }
    },

    onSearchKeydown: function(that) {
        // console.log('KEYDOWN: ');
        const ENTER = 13;

        if (e.keyCode == ENTER) {
            e.preventDefault();
            e.stopPropagation();

            const toAdd = jQuery("input[class=live-search-box]").val();
            if (toAdd) {
                jQuery("<li/>", {
                    text: toAdd,
                    "data-search-term": toAdd.toLowerCase()
                }).appendTo(jQuery("ul"));
                jQuery("input[class=live-search-box]").val("");
            }
        }
    },

    init: function() {
        var thisModule = this;
        console.log("Search init");
        thisModule.setSearchAttr();
        jQuery("body").on("keyup", ".live-search-box", function() {
            var searchTerm = thisModule.onSearchKeyup(this);
            console.log("searchTerm: " + searchTerm);

            // For ajaxCall, start the countdown

            clearTimeout(thisModule.typingTimer);
            thisModule.typingTimer = setTimeout(function() {
                // donetyping() method
                // TODO: Move to Tracker.js or create a child
                /* <fs_premium_only> */
                var data = {
                    action: "helpie_faq_search_counter",
                    nonce: thisModule.nonce,
                    searchTerm: searchTerm
                };

                jQuery.post(my_faq_ajax_object.ajax_url, data, function(
                    response
                ) {
                    // console.log(response);
                });
            }, thisModule.doneTypingInterval);
        });

        jQuery("input[class=live-search-box]").keydown(function(e) {
            thisModule.onSearchKeydown(this);

            // For ajaxCall, clear the countdown
            clearTimeout(thisModule.typingTimer);
        });
    }
};

module.exports = Stylus;
