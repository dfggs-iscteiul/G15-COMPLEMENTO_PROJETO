<?php

namespace Stylus\Components;

if (!defined('ABSPATH')) {
    exit;
} // Exit if accessed directly

if (!class_exists('\Stylus\Components\Accordion')) {

    class Accordion
    {

        public function __construct()
        {}

        public function get_view($viewProps)
        {
            $html = $viewProps['collection']['title'];
            $collectionProps = $viewProps['collection'];
            $top_level = $viewProps['collection']['display_mode'];;
            if( $top_level == 'simple_accordion_category'){
                $html = $this->get_titled_view($viewProps['items'], $collectionProps );
            } else{
                $html = $this->get_accordion($viewProps['items'], $collectionProps );
            }
            

            return $html;
        }

        public function show_cat_field($fieldName, $collectionProps){
            if( isset($collectionProps['display_category_fields']) 
                && is_array($collectionProps['display_category_fields']) 
                && in_array($fieldName, $collectionProps['display_category_fields'])
            ){
                return true;   
            }

            return false;
        }

        public function get_titled_view($props, $collectionProps ){
            $html = '';

            for ($ii = 0; $ii < sizeof($props); $ii++) {
                $html .= "<h3>" . $props[$ii]['title'] . "</h3>";
                if( $this->show_cat_field('description', $collectionProps) ){
                    $html .= "<p>" . $props[$ii]['content'] . "</p>";
                }
                $html .= $this->get_accordion($props[$ii]['children']);
            }

            return $html;
        }


        public function get_accordion($props){
            $html = '<article class="accordion">';

            for ($ii = 0; $ii < sizeof($props); $ii++) {
                $html .= $this->get_single_item($props[$ii]);
            }

            $html .= '</article>';

            return $html;
        }

        public function get_single_item($props)
        {
            $id = isset($props['post_id']) ? "post-".$props['post_id'] : "term-".$props['term_id'];

            $html = '<li class="accordion__item">';
            $html .= '<div class="accordion__header" data-id="'.$id.'">';
            $html .= '<div class="accordion__title">' . $props['title'] . '</div>';
            $html .= '</div>';
            $html .= '<div class="accordion__body">';
            $html .= '<p>' . $props['content'] . '</p>';

            if( isset($props['children'])){
                $html .= $this->get_accordion($props['children']);
            }
            
            $html .= '</div>';
            $html .= '</li>';

            return $html;
        }

        //     public function get_single_item_multilevel($props){
        //         $html = '<li>';
        //         $html .= '<div class="accordion__header">';
        //         $html .= '<div class="accordion__title">'.$props['title'].'</div>';
        //         $html .= '</div>';
        //         $html .= '<div class="accordion__body">';
        //         $html .= '<p>'.$props['content'].'</p>';
        //         $html .= $this->get_faq_dummy();
        //         $html .= '</div>';
        //         $html .= '</li>';

        //         return $html;
        //     }

        //     public function get_faq_dummy(){
        //         $html = '<div class="helpie-faq__container dark">';
        //         $html .= '<ul class="accordion">';
        //         for($ii = 1; $ii <= 2; $ii++){
        //             $html .= $this->get_single_item_dummy();
        //         }

        //         $html .= '</ul>';
        //         $html .= '</div>';

        //         return $html;
        //     }

        //    public function get_single_item_dummy(){
        //         $html = '<li>';
        //         $html .= '<div class="accordion__header">';
        //         $html .= '<div class="accordion__title">Title</div>';
        //         $html .= '</div>';
        //         $html .= '<div class="accordion__body">';
        //         $html .= '<p>Some Long Content here.</p>';
        //         $html .= '</div>';
        //         $html .= '</li>';
        //         return $html;
        //    }

    } // END CLASS
}
