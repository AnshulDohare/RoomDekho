package com.example.roomdekho.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.roomdekho.R;

public class PrivacyAndPolicy extends Fragment {

    WebView webView;
    public PrivacyAndPolicy() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_and_policy, container, false);
        webView = view.findViewById(R.id.privacyWebView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.freeprivacypolicy.com/live/252c27df-b40d-437a-83a1-2e2be4abe8b8");
        webView.getSettings().setJavaScriptEnabled(true);
        return view;
    }
}