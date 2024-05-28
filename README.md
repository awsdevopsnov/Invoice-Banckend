# IMS-BACKEND
Invoice Management System - Backend APIs


https://ims-backend-9ghn.onrender.com


https://reflectoring.io/spring-cors/


    @Bean
public WebMvcConfigurer corsMappingConfigurer() {
   return new WebMvcConfigurer() {
       @Override
       public void addCorsMappings(CorsRegistry registry) {
           WebConfigProperties.Cors cors = webConfigProperties.getCors();
           registry.addMapping("/**")
             .allowedOrigins(cors.getAllowedOrigins())
             .allowedMethods(cors.getAllowedMethods())
             .maxAge(cors.getMaxAge())
             .allowedHeaders(cors.getAllowedHeaders())
             .exposedHeaders(cors.getExposedHeaders());
       }
   };
}
