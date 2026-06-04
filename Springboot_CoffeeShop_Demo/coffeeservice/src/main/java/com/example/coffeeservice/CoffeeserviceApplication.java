package com.example.coffeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CoffeeserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeserviceApplication.class, args);
        System.out.println("Hello World");
    }

    // Requirement 2: GET request for /coffeeservice/test returning HTML
// Requirement 2: GET request for /coffeeservice/test 
    @GetMapping("/coffeeservice/test")
    public String coffeeTest() {
        return """
               <!DOCTYPE html>
               <html>
               <head>
                   <meta charset='UTF-8'>
                   <title>Welcome to the Coffee Shop</title>
                   <style>
                       body {
                           margin: 0;
                           padding: 0;
                           background-color: #fbf7f4; /* Soft warm cream background */
                           font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                           display: flex;
                           justify-content: center;
                           align-items: center;
                           height: 100vh;
                           color: #4a3b32; /* Deep warm brown text */
                       }
                       .card {
                           text-align: center;
                           background: #ffffff;
                           padding: 40px 60px;
                           border-radius: 24px;
                           box-shadow: 0 10px 30px rgba(184, 165, 153, 0.15); /* Soft, subtle shadow */
                           max-width: 400px;
                           border: 1px solid #f1e6df;
                       }
                       .icon-container {
                           font-size: 64px;
                           margin-bottom: 20px;
                           animation: float 3s ease-in-out infinite; /* Cute gentle floating animation */
                       }
                       h1 {
                           font-size: 28px;
                           margin: 10px 0;
                           font-weight: 600;
                           letter-spacing: -0.5px;
                       }
                       p {
                           font-size: 15px;
                           color: #8c7669; /* Soft muted brown for subtitle */
                           margin-bottom: 30px;
                           line-height: 1.6;
                       }
                       .accent-line {
                           width: 40px;
                           height: 3px;
                           background-color: #d4a373; /* Soft warm terracotta accent */
                           margin: 0 auto 20px auto;
                           border-radius: 2px;
                       }
                       @keyframes float {
                           0% { transform: translateY(0px); }
                           50% { transform: translateY(-10px); }
                           100% { transform: translateY(0px); }
                       }
                   </style>
               </head>
               <body>
                   <div class='card'>
                       <div class='icon-container'>☕</div>
                       <div class='accent-line'></div>
                       <h1>Welcome to my coffee shop</h1>
                       <p>Take a deep breath, settle in, and explore our freshly brewed digital catalog.</p>
                   </div>
               </body>
               </html>
               """; 
    }

    // NEW INTERACTIVE STEP: Displays an input box to the user
    @GetMapping("/coffeeservice/login")
    public String showLoginForm() {
        return """
               <html>
               <body style='font-family: Arial, sans-serif; margin: 40px;'>
                   <h2>Coffee Shop Portal</h2>
                   <form action='/coffeeservice/login/submit' method='GET'>
                       <label for='name'>Enter Your Name: </label>
                       <input type='text' id='name' name='name' required placeholder='Your name here...'>
                       <button type='submit'>Submit</button>
                   </form>
               </body>
               </html>
               """;
    }

    // Requirement 3: Processes the input submitted from the form above
    @GetMapping("/coffeeservice/login/submit")
    public String coffeeLogin(@RequestParam(value = "name") String name) {
        return "<h1>Welcome " + name + "! Ask catalog for menu!</h1>";
    }
}