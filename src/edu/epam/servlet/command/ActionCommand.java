package edu.epam.servlet.command;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionCommand {
    public String execute(HttpServletRequest request, HttpServletResponse response, Locale locale)
              throws ServletException, IOException, Exception;
}
