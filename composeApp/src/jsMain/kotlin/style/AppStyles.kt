package style

import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto

object AppStyles : StyleSheet() {

    init {

        /* Reset seguro sin romper buenas prácticas */

        universal style {
            property("box-sizing", "border-box")
        }

        "html, body" style {
            margin(0.px)
            padding(0.px)
            height(100.percent)
        }
    }

    /* Layout raíz */

    val layout by style {
        display(DisplayStyle.Flex)
        height(100.vh)
        fontFamily("Arial", "sans-serif")
        overflow("hidden")
    }

    /* Sidebar */

    val sidebar by style {
        width(220.px)
        height(100.percent)
        backgroundColor(Color("#386bff"))
        color(Color.white)
        padding(20.px)
    }

    val sidebarTitle by style {
        fontSize(22.px)
        fontWeight("bold")
        marginBottom(20.px)
    }

    val sidebarButton by style {

        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)

        width(100.percent)
        padding(12.px)
        marginBottom(8.px)

        backgroundColor(Color.transparent)
        color(Color.white)

        borderRadius(8.px)
        border { style(LineStyle.None) }

        fontSize(15.px)
        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {
            backgroundColor(Color("#1CB7FF"))
            property("transform", "translateX(4px)")
        }
    }

    val sidebarButtonActive by style {
        backgroundColor(Color("#1CB7FF"))
        fontWeight("bold")
    }

    val sidebarIcon by style {
        width(18.px)
        height(18.px)
        marginRight(10.px)
        property("filter", "brightness(0) invert(1)")
    }

    /* Content */

    val content by style {
        flexGrow(1)
        height(100.percent)
        overflowY("auto")

        padding(16.px, 24.px)

        backgroundColor(Color("#f4f6fb"))
    }

    val title by style {
        fontSize(26.px)
        fontWeight("bold")
        marginBottom(20.px)
    }

    /* Topbar */

    val topbar by style {

        backgroundColor(Color.white)

        height(64.px)

        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        justifyContent(JustifyContent.SpaceBetween)

        paddingLeft(24.px)
        paddingRight(24.px)

        property("border-bottom", "1px solid #ddd")
    }

    val username by style {
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        fontSize(14.px)
        fontWeight("bold")
    }

    val logoutButton by style {

        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)

        marginLeft(16.px)
        padding(6.px, 12.px)

        backgroundColor(Color("#e74c3c"))
        color(Color.white)

        borderRadius(6.px)
        border { style(LineStyle.None) }

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#A8331D"))
        }
    }

    val topbarIcon by style {
        width(24.px)
        height(24.px)
        marginRight(8.px)
    }

    /* Login */

    val loginContainer by style {

        display(DisplayStyle.Flex)
        height(100.vh)

        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)

        backgroundColor(Color("#f4f6fb"))
    }

    val loginCard by style {

        backgroundColor(Color.white)

        padding(42.px)

        borderRadius(14.px)

        width(340.px)

        property("box-shadow", "0 12px 32px rgba(0,0,0,0.08)")
    }

    val loginTitle by style {

        fontSize(24.px)
        fontWeight("600")

        marginBottom(26.px)

        color(Color("#1C56FF"))

        property("letter-spacing", "0.3px")
        property("text-align", "center")
    }

    val loginInput by style {

        width(100.percent)

        padding(12.px)
        marginBottom(18.px)

        borderRadius(8.px)

        border {
            style(LineStyle.Solid)
            width(1.px)
            color(Color("#D0D7DE"))
        }

        fontSize(14.px)

        property("transition", "0.15s ease")

        self + focus style {

            outline("none")

            border {
                color(Color("#1C56FF"))
                width(1.5.px)
            }

            property("box-shadow", "0 0 0 2px rgba(28,86,255,0.15)")
        }
    }

    val loginButton by style {

        width(100.percent)

        padding(13.px)

        backgroundColor(Color("#1C56FF"))
        color(Color.white)

        borderRadius(8.px)

        border { style(LineStyle.None) }

        fontSize(15.px)
        fontWeight("500")

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {
            backgroundColor(Color("#1747D1"))
        }

        width(100.percent)
    }

    /* Screen transition */

    val screenFade by style {

        property("animation", "fadeIn 0.25s ease")

        property(
            "@keyframes fadeIn",
            """
            from { opacity: 0; transform: translateY(6px); }
            to { opacity: 1; transform: translateY(0px); }
            """
        )
    }

    /* Loader */

    val loader by style {

        width(40.px)
        height(40.px)

        borderRadius(50.percent)

        border {
            width(4.px)
            style(LineStyle.Solid)
            color(Color("#1C56FF"))
        }

        property("border-top-color", "transparent")
        property("animation", "spin 1s linear infinite")

        property(
            "@keyframes spin",
            """
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
            """
        )
    }

    val loaderContainer by style {

        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)

        height(60.vh)
    }

    val dashboardGrid by style {

        display(DisplayStyle.Grid)

        property("grid-template-columns", "repeat(auto-fit, minmax(220px, 1fr))")

        gap(20.px)

        marginTop(20.px)
    }

    val statCard by style {

        backgroundColor(Color.white)

        borderRadius(12.px)

        padding(22.px)

        property("box-shadow", "0 6px 18px rgba(0,0,0,0.05)")

        property("transition", "0.15s ease")
    }

    val statTitle by style {

        fontSize(14.px)

        color(Color("#6b7280"))

        marginBottom(8.px)
    }

    val statValue by style {

        fontSize(28.px)

        fontWeight("bold")

        color(Color("#111827"))
    }

    val statAccent by style {

        height(4.px)

        borderRadius(6.px)

        marginBottom(10.px)

        backgroundColor(Color("#386bff"))
    }

    val statAccentBlue by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#386bff"))
    }

    val statAccentGreen by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#22c55e"))
    }

    val statAccentOrange by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#f59e0b"))
    }

    val statAccentRed by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#ef4444"))
    }

    val dialogOverlay by style {

        position(Position.Fixed)

        top(0.px)
        left(0.px)

        width(100.percent)
        height(100.percent)

        backgroundColor(Color("#ffffff"))

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)

        property("z-index", "999")
    }

    val dialogBox by style {

        backgroundColor(Color.white)

        padding(28.px)

        borderRadius(12.px)

        minWidth(280.px)

        property("box-shadow", "0 10px 30px rgba(0,0,0,0.15)")
    }

    val dialogButtons by style {

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.Center)

        marginTop(18.px)

        gap(10.px)
    }

    val dialogCancel by style {

        width(50.percent)

        padding(8.px, 14.px)

        backgroundColor(Color("#9ca3af"))

        color(Color.white)

        borderRadius(6.px)

        border { style(LineStyle.None) }

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#737780"))
        }
    }

    val dialogConfirm by style {

        width(50.percent)

        padding(8.px, 14.px)

        backgroundColor(Color("#19D600"))

        color(Color.white)

        borderRadius(6.px)

        border { style(LineStyle.None) }

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#179900"))
        }
    }

    val deleteButton by style {


        alignItems(AlignItems.Center)

        gap(6.px)

        padding(6.px, 10.px)

        backgroundColor(Color("#ef4444"))

        color(Color.white)

        borderRadius(6.px)

        border { style(LineStyle.None) }

        cursor("pointer")


        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#A8331D"))
        }

        width(75.percent)

    }

    val deleteIcon by style {

        width(14.px)
        height(14.px)

        property("filter", "brightness(0) invert(1)")
    }

    val tableContainer by style {

        maxWidth(1000.px)

        marginLeft(auto as CSSNumeric)

        marginRight(auto as CSSNumeric)

        backgroundColor(Color.white)

        borderRadius(16.px)

        padding(24.px)

        property("box-shadow", "0 4px 18px rgba(0,0,0,0.04)")
    }

    val table by style {

        width(100.percent)

        textAlign("center")
        alignItems("center")

        property("border-collapse", "separate")

        property("border-spacing", "0 12px")
    }

    val tableHeaderRow by style {

        backgroundColor(Color("#f8fafc"))
    }

    val tableHeader by style {


        color(Color("#6b7280"))

        padding(12.px)

        property("border-bottom", "1px solid #e5e7eb")
    }

    val tableRow by style {

        backgroundColor(Color("#F8FAFC"))

        borderRadius(12.px)
    }

    val tableCell by style {

        padding(14.px)

        fontSize(14.px)

        property("vertical-align", "middle")
    }

    val roleBadge by style {

        padding(4.px, 10.px)

        borderRadius(999.px)

        fontSize(12.px)

        fontWeight("600")

        property("display", "inline-block")

        property("text-transform", "capitalize")
    }

    val roleAdmin by style {

        backgroundColor(Color("#fee2e2"))

        color(Color("#b91c1c"))
    }

    val roleWorker by style {

        backgroundColor(Color("#dbeafe"))

        color(Color("#1d4ed8"))
    }

    val primaryButton by style {

        display(DisplayStyle.Flex)
        gap(6.px)

        padding(10.px, 16.px)

        backgroundColor(Color("#386bff"))

        color(Color.white)

        borderRadius(8.px)

        border {
            style(LineStyle.None)
        }

        fontSize(14.px)

        fontWeight("500")

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#2f55cc"))
        }
    }

    val addButton by style {

        padding(10.px, 16.px)

        backgroundColor(Color("#19D600"))

        color(Color.white)

        borderRadius(8.px)

        border {
            style(LineStyle.None)
        }

        fontSize(14.px)

        fontWeight("500")

        cursor("pointer")

        property("transition", "0.15s ease")
        property("display", "flex")
        property("align-items", "center")

        self + hover style {

            backgroundColor(Color("#179900"))
        }
    }

    val addIcon by style {

        width(24.px)
        height(24.px)
        marginRight(8.px)

        property("filter", "brightness(0) invert(1)")
    }

    val toast by style {

        position(Position.Fixed)

        bottom(40.px)

        left(50.percent)

        property("transform", "translateX(-50%)")

        backgroundColor(Color("#16a34a"))

        color(Color.white)

        padding(18.px, 28.px)

        borderRadius(12.px)

        fontSize(16.px)

        fontWeight("600")

        property("box-shadow", "0 8px 26px rgba(0,0,0,0.18)")

        property("animation", "fadeToast 0.35s ease")

        property(
            "@keyframes fadeToast",
            """
        from {
            opacity: 0;
            transform: translate(-50%, 12px);
        }
        to {
            opacity: 1;
            transform: translate(-50%, 0px);
        }
        """
        )
    }

    val badgeFichar by style {

        padding(6.px, 12.px)

        borderRadius(20.px)

        fontSize(13.px)

        fontWeight("600")
    }


    val badgeEntrada by style {

        backgroundColor(Color("#DCFCE7"))

        color(Color("#166534"))
    }


    val badgeSalida by style {

        backgroundColor(Color("#FEE2E2"))

        color(Color("#991B1B"))
    }

    val tableWrapperCentered by style {

        maxWidth(900.px)

        marginLeft(auto as CSSNumeric)

        marginRight(auto as CSSNumeric)
    }
    val secondaryButton by style {

        display(DisplayStyle.Flex)

        alignItems(AlignItems.Center)

        gap(8.px)

        padding(10.px, 16.px)

        backgroundColor(Color("#EEF2FF"))

        color(Color("#1C56FF"))

        borderRadius(10.px)

        border {
            style(LineStyle.None)
        }

        fontSize(14.px)

        fontWeight("600")

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#E0E7FF"))
        }
    }

    val buttonIcon by style {

        width(18.px)

        height(18.px)
    }

    val filterSelect by style {

        padding(8.px, 14.px)

        borderRadius(8.px)

        border {

            style(LineStyle.Solid)

            width(1.px)

            color(Color("#E2E8F0"))
        }

        backgroundColor(Color.white)

        fontSize(14.px)

        cursor("pointer")

        property("box-shadow", "0 1px 3px rgba(0,0,0,0.04)")
    }

    val badgeWarning by style {

        backgroundColor(Color("#FEF3C7"))
        color(Color("#92400E"))
    }

    val badgeDanger by style {

        backgroundColor(Color("#FEE2E2"))
        color(Color("#991B1B"))
    }

    val badgeInfo by style {

        backgroundColor(Color("#DBEAFE"))
        color(Color("#1E40AF"))
    }

    val rowApproved by style {

        backgroundColor(Color("#e8f8f1"))
    }


    val rowRejected by style {

        backgroundColor(Color("#fdecea"))
    }


    val approveButton by style {

        backgroundColor(Color("#2ecc71"))

        color(Color.white)

        border {
            style(LineStyle.None)
        }

        borderRadius(6.px)

        padding(6.px, 12.px)
        width(50.percent)

    }


    val rejectButton by style {

        backgroundColor(Color("#e74c3c"))

        color(Color.white)

        border {
            style(LineStyle.None)
        }

        borderRadius(6.px)

        padding(6.px, 12.px)

        width(50.percent)
    }

    val badgePendiente by style {

        backgroundColor(Color("#eef2ff"))
        color(Color("#3b5bdb"))

        padding(6.px, 14.px)

        borderRadius(12.px)

        fontWeight("500")
    }

    val badgeAprobado by style {

        backgroundColor(Color("#e8f8f1"))
        color(Color("#27ae60"))

        padding(6.px, 14.px)

        borderRadius(12.px)

        fontWeight("500")
    }

    val badgeRechazado by style {

        backgroundColor(Color("#fdecea"))
        color(Color("#e74c3c"))

        padding(6.px, 14.px)

        borderRadius(12.px)

        fontWeight("500")
    }

    val actionCell by style {

        height(6.vh)
        textAlign("center")
    }

    val actionButtonsGroup by style {

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.Center)

        alignItems(AlignItems.Center)

        gap(8.px)
    }

    val badgePdf by style {

        backgroundColor(Color("#fde68a"))
        color(Color("#92400e"))

        padding(6.px, 12.px)

        borderRadius(10.px)
    }

    val badgeDocumento by style {

        padding(6.px, 14.px)

        borderRadius(12.px)

        fontSize(13.px)

        fontWeight("500")

        property("display", "inline-block")
    }

    val badgeNomina by style {

        backgroundColor(Color("#dbeafe"))

        color(Color("#1e40af"))
    }

    val badgeReconocimiento by style {

        backgroundColor(Color("#fee2e2"))

        color(Color("#991b1b"))
    }

    val badgeFormacion by style {

        backgroundColor(Color("#dcfce7"))

        color(Color("#166534"))
    }

    val badgeEpis by style {

        backgroundColor(Color("#ede9fe"))

        color(Color("#5b21b6"))
    }




    val openButton by style {

        backgroundColor(Color("#3b82f6"))
        color(Color.white)

        border {
            style(LineStyle.None)
        }

        borderRadius(6.px)

        padding(6.px, 12.px)

        width(100.percent)
    }

    val badgeGreen by style {
        backgroundColor(rgb(220, 252, 231))
        color(rgb(22, 101, 52))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    val badgeRed by style {
        backgroundColor(rgb(254, 226, 226))
        color(rgb(153, 27, 27))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    val badgeBlue by style {
        backgroundColor(rgb(219, 234, 254))
        color(rgb(30, 64, 175))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    val badgeOrange by style {
        backgroundColor(rgb(255, 237, 213))
        color(rgb(154, 52, 18))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    val badgeDefault by style {
        backgroundColor(rgb(229, 231, 235))
        color(rgb(55, 65, 81))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }
}