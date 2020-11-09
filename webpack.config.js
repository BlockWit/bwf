const webpack = require('webpack');

const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

const config = {
    entry: {
        admin: {import: path.join(__dirname, 'src', 'main', 'assets', 'javascripts', 'admin.js')},
        main: {import: path.join(__dirname, 'src', 'main', 'assets', 'javascripts', 'main.js')}
    },
    output: {
        filename: path.join('js', '[name].js'),
        path: path.join(__dirname, 'target', 'classes', 'static'),
    },
    module: {
        rules: [
            {test: require.resolve('jquery'), loader: 'expose-loader', options: {exposes: ['$', 'jQuery']}},
            {test: /\.css$/, use: ['style-loader', {loader: 'css-loader', options: {importLoaders: 1}}, 'postcss-loader']},
            {test: /\.js$/, use: 'babel-loader', exclude: /node_modules/},
            {test: /\.png$/, use: [{loader: 'url-loader', options: {mimetype: 'image/png'}}]},
            {test: /\.scss$/, use: [MiniCssExtractPlugin.loader, 'css-loader', 'sass-loader']},
            {test: /\.svg$/, use: 'file-loader'},
            {test: /\.(woff|woff2|eot|ttf|otf)$/, use: 'file-loader'}
        ]
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: path.join('css', '[name].css'),
        }),
        new webpack.SourceMapDevToolPlugin({
            test: /\.js$/,
            append: '\n//# sourceMappingURL=[url]',
            filename: path.join('js', '[name][ext].map')
        }),
        new webpack.SourceMapDevToolPlugin({
            test: /\.css$/,
            append: '\n//# sourceMappingURL=[url]',
            filename: path.join('css', '[name][ext].map')
        })
    ]
};

module.exports = config;
