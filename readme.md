# 몰입캠프 1주차

Simple overview of use/purpose.

김성준, 정강산

# TAB 1 - 연락처

<p align="center"><img src="./readme_res/tab1.jpg" width=300px></p>

# TAB 2 - 갤러리

<p align="center"><img src="./readme_res/tab2.jpg" width=300px></p>

## **특징**

- 이미지 비율을 유지한 갤러리 구성을 보여준다

```java
Queue<String> imageList = ...

while (imageList.size() > 0) {
    double ratio1 = (double)bitmap1.getWidth() / bitmap1.getHeight();
    ...
    double ratio2 = (double)bitmap2.getWidth() / bitmap2.getHeight();

    // ratio1 과 ratio2를 비교, 적절한 배치를 결정
}

```

- 앱 시작 시 비동기적으로 휴대폰 저장소의 이미지들을 가져온다

```java
private class SetImageTask extends AsyncTask<Object, Void, Boolean> {
    private Queue<String> imagePaths;
    ...

    @Override
    // Set thread
    protected Boolean doInBackground(Object... params) {
        ...
        setImages(mRootView, imagePaths);
        return true;
    }

    @Override
    // Called when task done
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        ...
    }
```

## **최적화 이슈**

자원이 큰 이미지들을 사용하기 때문에 느리다

<해결책>

1. 이미지 리사이즈 (구현)

```java
Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
```

2. 현재 보이는 이미지들과 주변 이미지들만 캐싱 (미구현)

# TAB 3 - 틱택토 게임

<p align="center"><img src="./readme_res/tab3.gif" width=300px></p>

- [MiniMax](https://en.wikipedia.org/wiki/Minimax)
