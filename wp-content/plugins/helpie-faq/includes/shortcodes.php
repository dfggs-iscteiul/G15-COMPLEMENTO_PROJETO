<?php

namespace HelpieFaq\Includes;

if (!defined('ABSPATH')) {
    exit;
} // Exit if accessed directly

if (!class_exists('\HelpieFaq\Includes\Shortcodes')) {
    class Shortcodes
    {
        public function __construct()
        {
        }

        public static function basic($atts, $content = null)
        {

            $faq = new \HelpieFaq\Features\Faq\Faq();
            $faq_model = new \HelpieFaq\Features\Faq\Faq_Model();
            $defaults = $faq_model->get_default_args();
            $args = shortcode_atts($defaults, $atts);

            return $faq->get_view($args);
        }
    }
}

$helpie_faq_shortcodes = new \HelpieFaq\Includes\Shortcodes();

add_shortcode('helpie_faq', array($helpie_faq_shortcodes, 'basic'));
